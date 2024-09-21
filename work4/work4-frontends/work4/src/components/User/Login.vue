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

  export default {
    data() {
      return {
        username: '',
        password: ''
      };
    },
    methods: {
      async handleLogin() {
        try {
          const response = await login({ username: this.username, password: this.password });
          console.log('登录成功', response.data);
          // 登录成功后处理
        } catch (error) {
          console.error('登录失败', error);
        }
      }
    }
  };
  </script>
  