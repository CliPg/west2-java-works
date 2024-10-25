import axios from 'axios';

// 登录
export const login = (data) => {
    return axios.post('http://localhost:8080/user/login', data);
};

export const logout = (config) => {
    return axios.post('http://localhost:8080/user/logout',{},config);
};

// 注册
export const register = (data) => {
    return axios.post('http://localhost:8080/user/register', data);
};

// 获取用户信息
export const getUserInfo = (id) => {
    return axios.get(`http://localhost:8080/user/info?id=${id}`);
};

// 上传头像
export const uploadAvatar = (formData, config) => {
    return axios.post('http://localhost:8080/user/avatar/upload', formData, config);
};



