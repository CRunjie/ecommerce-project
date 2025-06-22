<template>
    <el-dialog title="登录"   v-model="visible" width="30%">
        <el-form ref="loginForm" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="用户名" prop="aname">
                <el-input v-model="form.aname" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="apwd">
                <el-input type="password" v-model="form.apwd" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleLogin(loginForm)">登录</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../axios/request.js'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = reactive({
    aname: '',
    apwd: ''
})
const visible = ref(true)
const rules = {
    aname: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    apwd: [
        { required: true, message: '请输入密码', trigger: 'blur' }
    ]
}
const loginForm = reactive({})
const handleLogin = async (formE) => {
    if (!formE) return
    await formE.validate((valid) => {
        if (valid) {
            request.myPost('/api/login', form).then(res => {
                if (res.data.code === 200) {
                    sessionStorage.setItem('aname', res.data.resData.aname)
                    sessionStorage.setItem('token', res.data.resData.token)
                    ElMessage.success('登录成功')
                    visible.value = false
                    router.push('/home')
                } else {
                    ElMessage.error('登录失败')
                }
            }).catch(err => {
                ElMessage.error('访问服务器失败')
            })
        } else {
            ElMessage.error('表单验证失败')
        }
    })
}
</script>

<style></style>