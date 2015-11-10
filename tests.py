import subprocess

### Don't touch this ###
try:
    from subprocess import DEVNULL # py3k
except ImportError:
    import os
    DEVNULL = open(os.devnull, 'wb')

cnt = 0

def test_case(desired_error, file_name):
    global cnt
    p = subprocess.Popen(['./compile', file_name], 
                         stdout=DEVNULL, stderr=DEVNULL)
    p.communicate()
    rc = p.returncode
    cnt += 1
    if rc != desired_error:
        print "WRONG ERROR CODE FOR ", file_name
        print "EXPECTED: ", desired_error, "  GOT: ", rc
        cnt -= 1
        p = subprocess.Popen(['./compile', file_name])
        print 
        

### Don't touch this ###


# VALID
valid_file_list = []
for root, dirs, files in os.walk('examples/valid'):
    for file in files:
        if file.endswith('.wacc'):
            filepath = os.path.join(root, file)
            valid_file_list.append(filepath)

cnt = 0
total_valid = len(valid_file_list)
print "### Running valid tests ###\n"
for file in valid_file_list:
    test_case(0, file)
print "### Valid tests complete ###\n"
valid_cnt = cnt


# SYNTAX
syntax_file_list = []
for root, dirs, files in os.walk('examples/invalid/syntaxErr'):
    for file in files:
        if file.endswith('.wacc'):
            filepath = os.path.join(root, file)
            syntax_file_list.append(filepath)

cnt = 0
total_syntax = len(syntax_file_list)
print "### Running syntax tests ###\n"
for file in syntax_file_list:
    test_case(100, file)
print "### Syntax tests complete ###\n"
syntax_cnt = cnt

# SEMANTIC
semantic_file_list = []
for root, dirs, files in os.walk('examples/invalid/semanticErr'):
    for file in files:
        if file.endswith('.wacc'):
            filepath = os.path.join(root, file)
            semantic_file_list.append(filepath)

cnt = 0
total_semantic = len(semantic_file_list)
print "### Running semantic tests ###\n"
for file in semantic_file_list:
    test_case(200, file)
print "### Semantic tests complete ###\n"
semantic_cnt = cnt

print "Valid : ", valid_cnt, "/", total_valid
print "Syntax: ", syntax_cnt, "/", total_syntax
print "Semantic: ", semantic_cnt, "/", total_semantic
        