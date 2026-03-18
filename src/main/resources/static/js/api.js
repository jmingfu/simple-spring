// 基础配置
const baseUrl = "http://localhost:8080"; // 你的后端地址

// axios 实例
const service = axios.create({
    baseURL: baseUrl,
    timeout: 5000,
    headers: {
        "Content-Type": "application/json;charset=utf-8"
    }
});

// 请求拦截器：自动带token（登录必备！）
service.interceptors.request.use(
    config => {
        let token = localStorage.getItem("token");
        if (token) {
            // 把 token 放进请求头
            config.headers["token"] = token;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;

        // ========== 核心：未登录 / 登录过期 自动跳转 ==========
        if (res.code === "ServerError" && res.msg === "请先登录") {
            // 清除本地登录信息
            localStorage.clear();
            // 提示
            ElementUI.Message.error("请先登录");
            // 跳登录页
            setTimeout(() => {
                window.location.href = "/login.html";
            }, 1000);
            return Promise.reject(res);
        }

        // 登录过期（你后端如果返回其他标识，我也能帮你加）
        if (res.code === "ServerError" && res.msg === "登录已过期") {
            localStorage.clear();
            ElementUI.Message.error("登录已过期，请重新登录");
            setTimeout(() => {
                window.location.href = "/login.html";
            }, 1000);
            return Promise.reject(res);
        }

        return res;
    },
    error => {
        // 401 未登录 → 自动跳登录
        if (error.response && error.response.status === 401) {
            Element.Message.error("请先登录");
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login.html"; // 登录页地址
        } else {
            Element.Message.error('网络异常，请检查连接');
        }
        return Promise.reject(error);
    }
);

// 核心接口
const api = {
    // 用户管理接口（你原来的）
    user: {
        list: (params) => service.get("/api/user/page", {params}),
        get: (id) => service.get(`/api/user?id=${id}`),
        add: (data) => service.post("api/user", data),
        update: (data) => service.put("/api/user", data),
        del: (id) => service.delete(`/api/user?id=${id}`),
        batchDel: (ids) => service.delete("/api/user/batch", {data: ids}),

        // ========== 新增：登录接口 ==========
        login: (data) => service.post("/admin/login", data),
        // ========== 新增：退出登录 ==========
        logout: () => service.post("/admin/logout"),
        register: (data) => service.post("/admin/register", data)
    },
    test: {
        hello: () => service.get("/api/hello")
    }
};