<template>
  <div class="login-container">
    <h2 class="login-title">登录</h2>
    <div class="login-form">
      <input v-model="username" placeholder="用户名" class="login-input" />
      <input v-model="password" placeholder="密码" type="password" class="login-input" />
      <button @click="handleLogin" class="login-button">登录</button>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user';
import { mapActions } from "vuex";

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
        if (response.data && response.data.code === 10000) {
          const token = response.data.data.token;
          const userInfo = { username: this.username };
          await this.setLoginData({ token, userInfo });
          this.$router.replace('/');
          console.log('登录成功', response.data);
        }
      } catch (error) {
        console.error('登录失败', error);
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 250px);
  background-color: #f0f2f5;
}

.login-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.login-form {
  width: 300px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.login-input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
}

.login-input:focus {
  border-color: #40a9ff;
  outline: none;
}

.login-button {
  width: 100%;
  padding: 10px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.login-button:hover {
  background-color: #40a9ff;
}
</style>

  