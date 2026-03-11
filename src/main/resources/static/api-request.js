// 通用接口请求函数
async function requestApi(url, method, data = {}) {
    try {
        const options = {
            method: method.toUpperCase(),
            headers: {
                "Content-Type": "application/json;charset=utf-8"
            }
        };

        // POST/PUT 请求添加请求体
        if (["POST", "PUT"].includes(options.method)) {
            options.body = JSON.stringify(data);
        }

        // GET/DELETE 请求拼接参数到URL
        if (["GET", "DELETE"].includes(options.method) && Object.keys(data).length > 0) {
            const params = new URLSearchParams(data);
            url += "?" + params.toString();
        }

        const response = await fetch(url, options);

        // 处理响应数据
        let responseData;
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            responseData = await response.json();
        } else {
            responseData = await response.text();
        }

        return {
            success: true,
            status: response.status,
            data: responseData
        };
    } catch (error) {
        return {
            success: false,
            message: "请求失败：" + error.message
        };
    }
}

// 渲染响应结果到页面
function renderResponse(elementId, result) {
    const responseContent = document.querySelector(`#${elementId} .response-content`);
    if (result.success) {
        responseContent.textContent = JSON.stringify({
            状态码: result.status,
            响应数据: result.data
        }, null, 2);
    } else {
        responseContent.textContent = "错误信息：" + result.message;
    }
}

// 重置表单
function resetForm(formId) {
    const form = document.getElementById(formId);
    form.reset();
    // 清空响应区域
    const responseId = formId.replace("form", "response");
    document.querySelector(`#${responseId} .response-content`).textContent = "";
}