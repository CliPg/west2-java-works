<template>
  <div class="logout-container">
    <h2 class="logout-title">退出登录</h2>
    <button @click="handleLogout" class="logout-button">退出登录</button>
  </div>
</template>

<script>
import { logout } from '@/api/user'; // 导入登出接口
import { mapActions, mapState } from 'vuex';
import { useRouter } from 'vue-router';

export default {
  computed: {
    ...mapState('user', ['token']), // 获取 token
  },
  methods: {
    ...mapActions('user', ['setLoginData']), // Vuex action 用于清除用户信息

    async handleLogout() {
      try {
        const response = await logout({
          headers: {
            Authorization: `Bearer ${this.token}`, // 在header中携带token
          },
        });

        if (response.data.code === 10000) {
          // 成功退出登录后，清除 Vuex 中的用户 token 和信息
          await this.setLoginData({ token: '', userInfo: {} });

          // 清除 localStorage 中的 token
          localStorage.removeItem('token');

          // 跳转到登录页面
          this.$router.replace('/user/login');

          console.log('退出登录成功');
        } else {
          console.error('退出登录失败', response.msg);
        }
      } catch (error) {
        console.error('退出登录请求失败', error);
      }
    },
  },
};
</script>

<style scoped>
.logout-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 250px); /* 上移一定高度 */
  background-color: #f0f2f5;
}

.logout-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.logout-button {
  padding: 10px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-button:hover {
  background-color: #40a9ff;
}
</style>


