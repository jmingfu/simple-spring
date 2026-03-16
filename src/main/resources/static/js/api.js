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

// 响应拦截器
service.interceptors.response.use(
    response => {
        // 交给页面自己处理业务逻辑
        if (response.status === 200) {
            return response.data;
        }
        // 非200状态码，统一拦截报错
        Element.Message.error('请求失败，服务器异常');
        return Promise.reject(response);
    },
    error => {
        // ❌ 这里只处理 网络错误、超时、404、500 等HTTP错误
        Element.Message.error('网络异常，请检查连接');
        return Promise.reject(error);
    }
);


// 核心接口（仅用户名+邮箱）
const api = {
    // 用户管理接口
    user: {
        // 分页查询
        list: (params) => service.get("/api/user/page", {params}),
        // 单条查询
        get: (id) => service.get(`/api/user?id=${id}`),
        // 新增
        add: (data) => service.post("api/user", data),
        // 修改
        update: (data) => service.put("/api/user", data),
        // 删除
        del: (id) => service.delete(`/api/user?id=${id}`),
        // 批量删除
        batchDel: (ids) => service.delete("/api/user/batch", {data: ids})
    },
    // 通用测试接口
    test: {
        hello: () => service.get("/api/hello")
    }
};