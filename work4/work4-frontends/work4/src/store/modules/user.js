import { getUserInfo } from '@/api/user';

const state = {
    token: localStorage.getItem('token') || '',
    userInfo: {},
};

const mutations = {
    SET_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
    },
    SET_USER_INFO(state, info) {
        state.userInfo = info;
    },
};

const actions = {
    // 设置 token 和用户信息
    setLoginData({commit}, { token, userInfo }) {
        commit('SET_TOKEN', token);
        commit('SET_USER_INFO', userInfo);
    },
    async fetchUserInfo({ commit, state }) {
        if (state.token) {
            const response = await getUserInfo(state.token);
            commit('SET_USER_INFO', response.data);
        }
    },
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
};


