## 未使用 git add 缓存代码时
git checkout -- <filename>
git rm --cached “文件路径”不删除物理文件，仅将该文件从缓存中删除；
git rm --f “文件路径”，不仅将该文件从缓存中删除，还会将物理文件删除
## 撤回暂存区所有文件
git reset HEAD .
撤回暂存区指定的文件
git reset HEAD filename
## 撤销commit
git reset --soft HEAD^ 
git reset --hard HEAD^ 
git reset --hard commit_id

## 分支
https://blog.csdn.net/csflvcxx/article/details/81612336
创建新分支：git branch branchName
切换到新分支：git checkout branchName
上面两个命令也可以合成为一个命令：
git checkout -b branchName