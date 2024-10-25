<template>
  <a-layout>
    <a-layout-header class="header">
      <div class="logo" />
      <a-menu
          v-model:selectedKeys="selectedKeys1"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1">
          <router-link to="/">Home</router-link>
        </a-menu-item>
        <a-menu-item key="2">
          <router-link to="/user">User</router-link>
        </a-menu-item>
        <a-menu-item key="3">
          <router-link to="/video">Video</router-link>
        </a-menu-item>
        <a-menu-item key="4">
          <router-link to="/interaction">Interaction</router-link>
        </a-menu-item>
        <a-menu-item key="5">
          <router-link to="/social">Social</router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-header>

  </a-layout>
</template>

<script setup>
import { ref } from 'vue';
const selectedKeys1 = ref(['2']);
const selectedKeys2 = ref(['1']);
const openKeys = ref(['sub1']);
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background-color: #001529;
}

.logo {
  width: 120px;
  height: 31px;
  margin: 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.site-layout-background {
  background: #fff;
}
</style>
