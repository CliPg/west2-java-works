<template>
  <div class="register-container">
    <h2 class="register-title">注册</h2>
    <div class="register-form">
      <input v-model="username" placeholder="用户名" class="register-input" />
      <input v-model="password" placeholder="密码" type="password" class="register-input" />
      <button @click="handleRegister" class="register-button">注册</button>
    </div>
  </div>
</template>

<script>
import { register } from '@/api/user';

export default {
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async handleRegister() {
      try {
        const response = await register({ username: this.username, password: this.password });
        console.log('注册成功', response.data);
        // 注册成功后处理，比如重定向到登录页面
      } catch (error) {
        console.error('注册失败', error);
      }
    }
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 250px); /* 上移一定高度 */
  background-color: #f0f2f5;
}

.register-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.register-form {
  width: 300px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.register-input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
}

.register-input:focus {
  border-color: #40a9ff;
  outline: none;
}

.register-button {
  width: 100%;
  padding: 10px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.register-button:hover {
  background-color: #40a9ff;
}
</style>

  