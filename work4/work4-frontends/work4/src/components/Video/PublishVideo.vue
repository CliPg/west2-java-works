<template>
  <div class="upload-container">
    <h2 class="upload-title">上传视频</h2>
    <input v-model="title" placeholder="视频标题" class="upload-input" />
    <input v-model="description" placeholder="视频描述" class="upload-input" />
    <input type="file" @change="handleFileChange" class="file-input" />
    <button @click="handleUpload" :disabled="!isLoggedIn" class="upload-button">上传</button>
    <p v-if="!isLoggedIn" class="login-warning">请先登录以上传视频。</p>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { publishVideo } from "@/api/video";

export default {
  data() {
    return {
      title: '',
      description: '',
      videoFile: null,
    };
  },
  computed: {
    ...mapState('user', ['token']),
    isLoggedIn() {
      return !!this.token; // 判断用户是否已登录
    },
  },
  methods: {
    handleFileChange(event) {
      this.videoFile = event.target.files[0]; // 获取文件
    },
    async handleUpload() {
      if (!this.videoFile) {
        console.error("请先选择一个文件");
        return;
      }

      const formData = new FormData();
      formData.append('video', this.videoFile); // 添加视频文件
      formData.append('title', this.title); // 添加标题
      formData.append('description', this.description); // 添加描述

      try {
        const response = await publishVideo(formData, {
          headers: {
            Authorization: `Bearer ${this.token}`, // 携带 token
            'Content-Type': 'multipart/form-data' // 设置 multipart
          }
        });
        console.log('视频上传成功', response.data);
      } catch (error) {
        console.error('视频上传失败', error);
      }
    },
  },
};
</script>

<style scoped>
.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 100px); /* 上移一定高度 */
  background-color: #f0f2f5;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.upload-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.upload-input {
  width: 100%;
  max-width: 400px;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.file-input {
  margin: 10px 0;
}

.upload-button {
  padding: 10px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.upload-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.upload-button:hover:not(:disabled) {
  background-color: #40a9ff;
}

.login-warning {
  color: #ff4d4f;
  margin-top: 10px;
}
</style>


