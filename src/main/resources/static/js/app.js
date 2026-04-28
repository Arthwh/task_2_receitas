const {createApp, ref, reactive, onMounted} = Vue;

createApp({
    setup() {
        const isLoggedIn = ref(false);
        const userLogin = ref(localStorage.getItem('user_login') || '');
        const receitas = ref([]);

        // Estados dos Filtros
        const filtroTipo = ref('');
        const filtroData = ref('');

        // Estados dos formulários
        const authForm = reactive({login: '', senha: ''});
        const createUserForm = reactive({nome: '', login: '', senha: ''});
        const recipeForm = reactive({nome: '', descricao: '', custo: 0, tipoReceita: 'SALGADO', addedAt: ''});
        const userForm = reactive({id: null, nome: '', login: '', senha: ''});

        // Funções de Autenticação
        const login = async () => {
            try {
                const res = await api.post('/auth/login', authForm);
                localStorage.setItem('jwt_token', res.data.token);
                localStorage.setItem('user_login', authForm.login);
                isLoggedIn.value = true;
                userLogin.value = authForm.login;
                await loadRecipes();
            } catch (e) {
                alert("Login inválido!");
            }
        };

        const loadRecipes = async () => {
            try {
                // Monta o objeto de parâmetros
                const params = {};

                if (filtroTipo.value) params.tipoReceita = filtroTipo.value;
                if (filtroData.value) params.dataInicio = filtroData.value;

                console.log(params);

                // Faz a chamada: /receitas?tipo=DOCE&data=2024-05-20
                const res = await api.get('/receitas', { params });
                console.log(res.data);
                receitas.value = res.data;
            } catch (e) {
                console.error("Erro ao carregar receitas do servidor");
            }
        };

        const loadUserData = async () => {
            try {
                const res = await api.get(`/usuarios/perfil/${userLogin.value}`);
                // Popula o formulário com os dados que vieram do banco
                userForm.id = res.data.id;
                userForm.nome = res.data.nome;
                userForm.login = res.data.login;
                userForm.senha = '';
            } catch (e) {
                console.error("Erro ao carregar perfil");
            }
        };

        // Gerenciamento de Modais
        const showModal = (id) => {
            const modalElem = document.getElementById(id);
            const instance = bootstrap.Modal.getOrCreateInstance(modalElem);
            instance.show();
        };

        const hideModal = (id) => {
            const instance = bootstrap.Modal.getInstance(document.getElementById(id));
            if (instance) instance.hide();
        };

        const saveRecipe = async () => {
            await api.post('/receitas', {...recipeForm, login: userLogin.value});
            alert("Receita salva com sucesso!");
            hideModal('recipeModal');
            await loadRecipes();
        };

        const createUser = async () => {
            await api.post('/auth/signup', {...createUserForm});
            alert("Usuário criado com sucesso!");
            hideModal('createUserModal');
        };

        const updateUser = async () => {
            await api.put('/usuario', {...userForm});
            alert("Usuário atualizado com sucesso!");
            hideModal('createUserModal');
        };

        const exportPdf = async () => {
            try {
                const response = await api.get('/receitas/exportar', {
                    params: {
                        tipo: filtroTipo.value,
                        data: filtroData.value
                    },
                    responseType: 'blob' // Importante para arquivos binários
                });

                // Cria um link temporário para o navegador baixar o arquivo
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'relatorio_receitas.pdf');
                document.body.appendChild(link);
                link.click();
                link.remove();
            } catch (e) {
                alert("Erro ao gerar PDF. Verifique se o backend está ativo.");
            }
        };

        const clearFilters = () => {
            filtroTipo.value = '';
            filtroData.value = '';
            loadRecipes(); // Recarrega a lista completa
        };

        const logout = () => {
            localStorage.clear();
            location.reload();
        };

        const formatDate = (timestamp) => {
            if (!timestamp) return '';
            // Converte o timestamp para um objeto Date do JavaScript
            const data = new Date(timestamp);
            // Formata para o padrão brasileiro
            // return data.toLocaleDateString('pt-BR');

            // Se você também quiser a hora, use:
            return data.toLocaleString('pt-BR');
        };

        onMounted(() => {
            if (localStorage.getItem('jwt_token')) {
                isLoggedIn.value = true;
                loadRecipes();
                loadUserData();
            }
        });

        return {
            isLoggedIn, userLogin, receitas, authForm, recipeForm, userForm, filtroTipo, filtroData,
            login, logout, showModal, saveRecipe, createUser, updateUser, exportPdf, clearFilters, loadRecipes, loadUserData, formatDate
        };
    }
}).mount('#app');