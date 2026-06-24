const api = axios.create({
    baseURL: globalThis.location.origin
});

// Interceptor para injetar o Token em cada chamada
api.interceptors.request.use(config => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Interceptor para erros
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            console.warn("Sessão inválida ou expirada. Forçando logout...");
            localStorage.clear();
            location.reload();
        }
        return Promise.reject(error);
    }
);