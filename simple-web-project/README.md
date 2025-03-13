# 环境

## node

node -v
v16.17.0

## npm

npm -v
8.15.0

## 技术栈

axios  
图标: https://www.iconfont.cn/   
element-plus  https://element-plus.org/zh-CN/component/overview.html  
mitt 组件通信  
pinia 装填管理  
vue-router 路由  
tailwindcss 官网 https://www.tailwindcss.cn/docs/installation  
富文本编辑器wangeditor  https://www.wangeditor.com/ 
```
"@wangeditor/editor": "^5.1.23",
"@wangeditor/editor-for-vue": "^5.1.12"
```



## 安装tailwindcss
#### 安装
npm install -D tailwindcss postcss autoprefixer  
#### 生成配置文件 
npx tailwindcss init -p  
#### 配置文件 tailwind.config.js 
```
/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{html,ts,tsx,js,jsx,tsx,vue}"],
  theme: {
    extend: {},
  },
  plugins: [],
}
```
#### src目录中创建tailwindcss.css文件
```
@tailwind base;
@tailwind components;
@tailwind utilities;
```

#### main.ts 引入 
```
// 引入tailwindcss
import "./tailwindcss.css"
```


# 图标
https://www.iconfont.cn/