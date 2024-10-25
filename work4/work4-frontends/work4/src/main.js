import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 确保正确导入
import store from './store';
import Antd from 'ant-design-vue';
//import 'ant-design-vue/es/style.css';


const app = createApp(App);
app.use(router); // 使用路由
app.use(store); // 使用状态管理
app.use(Antd); // 使用Ant Design Vue组件库
app.mount('#app');

