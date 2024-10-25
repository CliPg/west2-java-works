<template>
    <div>
      <h2>用户信息</h2>
      <div v-if="userInfo">
        <p>用户名: {{ userInfo.username }}</p>
        <p>邮箱: {{ userInfo.email }}</p>
      </div>
    </div>
  </template>
  
  <script>
  import { getUserInfo } from '@/api/user';
  
  export default {
    data() {
      return {
        userInfo: null
      };
    },
    async created() {
      try {
        const response = await getUserInfo();
        this.userInfo = response.data;
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    }
  };
  </script>
  