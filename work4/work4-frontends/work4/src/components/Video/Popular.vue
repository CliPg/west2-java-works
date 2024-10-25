<template>
  <div class="popular-videos-container">
    <h2 class="title">热门视频排行榜</h2>
    <ul v-if="videos.length" class="video-list">
      <li v-for="(video, index) in videos" :key="video.id" class="video-item">
        <h3 class="video-title">{{ index + 1 }}. {{ video.title }}</h3>
        <p class="video-info">上传用户: {{ video.userId }}</p>
        <p class="video-stats">观看次数: {{ video.visitCount }} | 点赞数: {{ video.likeCount }} | 评论数: {{ video.commentCount }}</p>
        <p class="upload-time">上传时间: {{ formatDate(video.createTime) }}</p>
        <video v-if="video.videoUrl" controls width="300" class="video-player">
          <source :src="video.videoUrl" type="video/mp4" />
          您的浏览器不支持视频标签。
        </video>
      </li>
    </ul>
    <p v-else class="no-results">暂时没有热门视频</p>
  </div>
</template>

<script>
import { popularVideo } from "@/api/video";
import { mapState } from "vuex";

export default {
  data() {
    return {
      videos: [], // 保存视频列表
    };
  },
  mounted() {
    // 组件加载时获取热门视频
    this.fetchPopularVideos();
  },
  computed: {
    ...mapState('user', ['token']),
  },
  methods: {
    async fetchPopularVideos() {
      try {
        const response = await popularVideo({
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });
        if (response.data.code === 10000) {
          this.videos = response.data.data.items;
        } else {
          console.error('获取热门视频失败:', response.data.message);
        }
      } catch (error) {
        console.error('请求热门视频失败:', error);
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
.popular-videos-container {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
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
.video-stats,
.upload-time {
  margin: 5px 0;
  color: #666;
}

.video-player {
  margin-top: 10px;
  border-radius: 5px;
}

.no-results {
  text-align: center;
  color: #999;
}
</style>


