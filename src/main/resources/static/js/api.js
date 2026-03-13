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
        const res = response.data;
        if (res.code !== 200 && res.code !== undefined) {
            Element.Message.error(res.msg || "操作失败");
            return Promise.reject(res);
        }
        return res;
    },
    error => {
        Element.Message.error(error.message || "服务器错误");
        return Promise.reject(error);
    }
);

// 核心接口（仅用户名+邮箱）
const api = {
    // 用户管理接口
    user: {
        // 分页查询
        list: (params) => service.get("/user/list", { params }),
        // 单条查询
        get: (id) => service.get(`/api/user/${id}`),
        // 新增
        add: (data) => service.post("/user/add", data),
        // 修改
        update: (data) => service.put("/api/user", data),
        // 删除
        del: (id) => service.delete(`/api/user/${id}`),
        // 批量删除
        batchDel: (ids) => service.delete("/api/user/batch", { data: ids })
    },
    // 通用测试接口
    test: {
        hello: () => service.get("/api/hello")
    }
};