<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu mode="inline" :style="{ height: '100%', borderRight: 0 }">
        <a-sub-menu key="sub1">
          <template #title>
            <span>User Menu</span>
          </template>
          <a-menu-item key="login">
            <router-link to="/user/login">登录</router-link>
          </a-menu-item>
          <a-menu-item key="register">
            <router-link to="/user/register">注册</router-link>
          </a-menu-item>
          <a-menu-item key="logout">
            <router-link to="/user/logout">退出登录</router-link>
          </a-menu-item>
          <a-menu-item key="uploadAvatar">
            <router-link to="/user/avatar/upload">上传头像</router-link>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '750px' }"
      >
        <router-view /> <!-- 用于显示子路由内容 -->
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup>
// 不需要显式定义组件
</script>

<style scoped>
/* 在这里添加样式 */
</style>


  