## 生物信息资源管理
#### 技术
+ SpringBoot
+ mysql

#### 功能
+ 项目报告管理
+ 癌症数据资源管理

#### 联系
+ Email: 1749748955@qq.com
+ Email: 3300297450@qq.com

#### 代码贡献
```
git clone https://github.com/BioinfoFungi/bioinfo_analysis.git
```
#### 迁移后提交
+ 方法有三种
    + 修改命令:git remote origin set-url [url]
    + 先删后加
        + git remote rm origin
        + git remote add origin [url]
    + 直接修改config文件
```
git remote add origin_1 https://github.com/BioinfoFungi/bioinfo_analysis.git
git add .
git commit -m 'XXXX'
git status
git push origin_1 master
```