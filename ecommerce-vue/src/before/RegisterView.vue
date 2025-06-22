<template>
  <el-dialog title="用户注册" v-model="dialogVisible" width="35%" @close="goClose()">
    <div class="box">
      <el-form ref="registerFormRef" :model="registerForm" :rules="rules" style="width:100%;" label-width="20%">
        <el-form-item label="Email" prop="bemail">
          <el-input v-model="registerForm.bemail" placeholder="请输入Email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="bpwd">
          <el-input show-password v-model="registerForm.bpwd" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="reupwd">
          <el-input show-password v-model="registerForm.reupwd" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register(registerFormRef)">{{ loadingbuttext }}</el-button>
          <el-button type="danger" @click="cancel(registerFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script setup>
import {useRoute, useRouter} from 'vue-router'
import { reactive} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../axios/request.js'
const router = useRouter()
const route = useRoute() 
const registerFormRef = reactive({})
const registerForm = reactive({})
//验证规则
const rules = reactive( {
    bemail: [{ required: true, message: '请输入Email', trigger: 'blur' }],
    bpwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度6到20', trigger: 'blur' }
        ],
    reupwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度6到20', trigger: 'blur' }
        ]
    })
let  loadingbuttext = '注册'
let  dialogVisible = true
const   register =  async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      axios.myPost('/api/before/bUser/register', registerForm)
      .then(res => {
        if (res.data.code  === 305){
          ElMessage.error(res.data.msg)
        } else if (res.data.code  === 200){
          ElMessage({message: '注册成功',type: 'success'})
          //path为跳转到前一个页面
          let path = route.query.redirect
          router.replace({ path: path === '/' || path === undefined ? '/login' : path })
        } else {
          ElMessage.error('注册失败！')
        }
      })
      .catch(() => {
          ElMessage.error('访问异常')
      })
    } else {
      console.log('error submit!', fields)
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
  height: 180px;
}
</style>