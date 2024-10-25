<template>
  <div class="video-search-container">
    <h2 class="search-title">视频搜索</h2>
    <div class="search-bar">
      <input v-model="keywords" placeholder="输入关键词搜索" class="search-input" />
      <button @click="handleSearch" class="search-button">搜索</button>
    </div>
    <ul v-if="videos.length" class="video-list">
      <li v-for="(video, index) in videos" :key="video.id" class="video-item">
        <h3 class="video-title">{{ video.title }}</h3>
        <p class="video-info">上传用户: {{ video.userId }}</p>
        <p class="video-description">描述: {{ video.description }}</p>
        <p class="video-stats">观看次数: {{ video.visitCount }} | 点赞数: {{ video.likeCount }} | 评论数: {{ video.commentCount }}</p>
        <p class="upload-time">上传时间: {{ formatDate(video.createTime) }}</p>
        <video v-if="video.videoUrl" controls class="video-player">
          <source :src="video.videoUrl" type="video/mp4" />
          您的浏览器不支持视频标签。
        </video>
      </li>
    </ul>
    <p v-else class="no-results">暂时没有搜索结果</p>
  </div>
</template>

<script>
import { searchVideo } from "@/api/video"; // 确保引入路径正确
import { mapState } from "vuex";

export default {
  data() {
    return {
      keywords: '', // 搜索关键词
      videos: [],   // 保存视频列表
    };
  },
  computed: {
    ...mapState('user', ['token']),
  },
  methods: {
    async handleSearch() {
      if (!this.keywords) {
        alert("请输入搜索关键词");
        return;
      }

      try {
        const formData = new FormData();
        formData.append('keywords', this.keywords);
        formData.append('pageNum', 1);
        formData.append('pageSize', 10);
        const response = await searchVideo(formData, {
          headers: {
            Authorization: `Bearer ${this.token}`, // 携带 token
          }
        });

        if (response.data.code === 10000) { // 假设 10000 是成功代码
          this.videos = response.data.data.items; // 获取视频列表
        } else {
          console.error('搜索失败:', response.data.message);
          this.videos = []; // 清空视频列表
        }
      } catch (error) {
        console.error('请求搜索失败:', error);
      }
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString(); // 根据需要格式化日期
    },
  },
};
</script>

<style scoped>
.video-search-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.search-title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.search-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
}

.search-button {
  padding: 10px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #40a9ff;
}

.video-list {
  list-style-type: none;
  padding: 0;
}

.video-item {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.video-title {
  font-size: 20px;
  margin: 0;
}

.video-info,
.video-description,
.video-stats,
.upload-time {
  margin: 5px 0;
  color: #666;
}

.video-player {
  width: 100%;
  max-width: 300px;
  margin-top: 10px;
}

.no-results {
  text-align: center;
  color: #999;
}
</style>


