<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu mode="inline" :style="{ height: '100%', borderRight: 0 }">
        <a-sub-menu key="sub1">
          <template #title>
            <span>Video Menu</span>
          </template>
          <a-menu-item key="login">
            <router-link to="/video/publish">上传视频</router-link>
          </a-menu-item>
          <a-menu-item key="register">
            <router-link to="/video/search">搜索视频</router-link>
          </a-menu-item>
          <a-menu-item key="logout">
            <router-link to="/video/popular">热门视频</router-link>
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
<!--<template>-->
<!--  <div>-->
<!--    <h1>视频模块</h1>-->
<!--    <router-view></router-view>-->
<!--    <router-link to="/video/publish">上传视频</router-link> |-->
<!--    <router-link to="/video/search">搜索视频</router-link> |-->
<!--    <router-link to="/video/popular">热门视频</router-link>-->

<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import Login from '@/components/User/Login';-->
<!--import Register from '@/components/User/Register';-->
<!--import UserInfo from '@/components/User/UserInfo';-->
<!--import AvatarUpload from '@/components/User/AvatarUpload';-->

<!--export default {-->
<!--  components: {-->
<!--  }-->
<!--};-->
<!--</script>-->