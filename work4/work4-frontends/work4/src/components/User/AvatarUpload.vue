<template>
  <div class="avatar-upload-container">
    <h2 class="upload-title">上传头像</h2>
    <input type="file" @change="handleFileChange" class="file-input" />
    <button @click="handleUpload" :disabled="!isLoggedIn" class="upload-button">上传</button>
    <p v-if="!isLoggedIn" class="login-warning">请先登录以上传头像。</p>
  </div>
</template>

<script>
import { uploadAvatar } from '@/api/user';
import { mapState } from 'vuex';

export default {
  data() {
    return {
      avatarFile: null,
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
      this.avatarFile = event.target.files[0]; // 获取文件
    },
    async handleUpload() {
      if (!this.avatarFile) {
        console.error("请先选择一个文件");
        return;
      }

      const formData = new FormData();
      formData.append('avatar', this.avatarFile);

      try {
        const response = await uploadAvatar(formData, {
          headers: {
            Authorization: `Bearer ${this.token}` // 在header中携带token
          }
        });
        console.log('头像上传成功', response.data);
      } catch (error) {
        console.error('头像上传失败', error);
      }
    },
  },
};
</script>

<style scoped>
.avatar-upload-container {
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




  