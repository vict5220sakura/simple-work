import { createSSRApp } from "vue";
import App from "./App.vue";
import * as Pinia from 'pinia';
// 引入持久化插件
import persist from 'pinia-plugin-persistedstate'

const pinia = Pinia.createPinia()
// 使用持久化插件
pinia.use(persist)

export function createApp() {
  const app = createSSRApp(App);
  app.use(pinia);

  return {
    app,
    Pinia, // 此处必须将 Pinia 返回
  };
}
