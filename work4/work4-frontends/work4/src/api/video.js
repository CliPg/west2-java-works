import axios from 'axios';

// 上传视频
export const publishVideo = (formData, config) => {
    return axios.post('http://localhost:8080/video/publish', formData, config);
};

export const popularVideo = (config) => {
    console.log(config);
    return axios.get('http://localhost:8080/video/popular', config);
};

export const searchVideo = (formData,config) => {
    console.log(config);
    return axios.post('http://localhost:8080/video/search', formData,config);
};