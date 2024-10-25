<template>
  <nav>
<!--    <router-link to="/">Home</router-link> |-->
<!--    <router-link to="/user">User</router-link> |-->
<!--    <router-link to="/video">Video</router-link> |-->
<!--    <router-link to="/interaction">Interaction</router-link> |-->
<!--    <router-link to="/Social">Social</router-link>-->
    <LayoutComponent/>
  </nav>
  <router-view/>
</template>

<script setup>
import LayoutComponent from './components/LayoutComponent.vue';
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
