<template>
  <el-dialog title="用户登录" v-model="dialogVisible" width="35%" @close="goClose()">
    <div class="box">
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" style="width:100%;" label-width="20%">
        <el-form-item label="Email" prop="bemail">
          <el-input v-model="loginForm.bemail" placeholder="请输入Email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="bpwd">
          <el-input show-password v-model="loginForm.bpwd" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
            <el-input placeholder="请输入验证码" v-model="loginForm.code"></el-input>
            <img :src="checkcodeurl" @click="getcode">
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register(loginFormRef)">{{ loadingbuttext }}</el-button>
          <el-button type="danger" @click="cancel(loginFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script setup>
import {useRoute, useRouter} from 'vue-router'
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
const router = useRouter()
const route = useRoute() 
const loginFormRef = reactive({})
const loginForm = reactive({})
import axios from '../axios/request.js'
//验证规则
const rules = reactive( {
    bemail: [{ required: true, message: '请输入Email', trigger: 'blur' }],
    bpwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度6到20', trigger: 'blur' }
      ],
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
    })
let  loadingbuttext = '登录'
let  dialogVisible = true
const  checkcodeurl = ref(axios.baseUrl + "/before/bUser/getCode?t="+new Date().getTime())
const getcode = ()=>{
    checkcodeurl.value= axios.baseUrl + "/before/bUser/getCode?"+new Date().getTime()
}
const   register =  async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      axios.myPost('/api/before/bUser/login', loginForm)
      .then(res => {
        if(res.data.code  === 306){
          ElMessage.error(res.data.msg)
        } else if (res.data.code  === 301){
          ElMessage.error(res.data.msg)
        } else if (res.data.code  === 300){
          ElMessage.error(res.data.msg)
        } else {
          ElMessage({message: '登录成功', type: 'success'})
          sessionStorage.setItem("token", res.data.resData.token);
					sessionStorage.setItem("bemail", res.data.resData.bemail);
          sessionStorage.setItem("bid", res.data.resData.id);
          let path = route.query.redirect
          router.replace({ path: path === '/' || path === undefined ? '/' : path })
        }
      })
      .catch(() => {
          ElMessage.error('登录异常')
      })
    } else {
      ElMessageBox.alert(
        '<span style="color: rgb(249, 7, 7); font-size: 12pt;">表单验证失败！</span>',
        '', {dangerouslyUseHTMLString: true}
      )
    }
  })
}
 const  cancel = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
const goClose = () => {
    router.replace('/')
}
</script>
<style scoped>
.box {
  width: 100%;
  height: 200px;
}
</style>