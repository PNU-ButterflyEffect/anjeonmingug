import os


f = open('./untracked.txt', 'r')
while True:
    line = f.readline()
    if not line: break
    bashCommand = "git rm --cached " + line
    os.system(bashCommand)
