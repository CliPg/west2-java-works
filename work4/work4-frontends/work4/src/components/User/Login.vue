<template>
    <div>
      <h2>登录</h2>
      <input v-model="username" placeholder="用户名" />
      <input v-model="password" placeholder="密码" type="password" />
      <button @click="handleLogin">登录</button>
    </div>
  </template>

  <script>
  import { login } from '@/api/user';
  import {mapActions} from "vuex";

  export default {
    data() {
      return {
        username: '',
        password: ''
      };
    },
    methods: {
      ...mapActions('user', ['setLoginData']),
      async handleLogin() {
        try {
          const response = await login({ username: this.username, password: this.password });
          if (response.data && response.data.code === 10000) { // 检查返回的 code
            const token = response.data.data.token; // 提取 token
            const userInfo = { username: this.username }; // 可以在这里添加更多用户信息

            // 使用 Vuex 设置 token 和用户信息
            await this.setLoginData({ token, userInfo });

            // 登录成功后重定向到主页
            this.$router.replace('/'); // 使用 Vue Router
            console.log('登录成功', response.data);
          }
          //console.log('登录成功', response.data);
          // 登录成功后处理
        } catch (error) {
          console.error('登录失败', error);
        }
      }
    }
  };
  </script>
  