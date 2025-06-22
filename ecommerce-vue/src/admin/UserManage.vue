<template>
  <el-tabs type="border-card">
    <el-tab-pane label="用户管理">
      <div style="margin-top: -10px;margin-bottom: 10px;">
        <el-button size="medium" type="success" @click="openAdd()">添加用户</el-button>
        <div style="float: right; width: 300px;">
          <el-input v-model="searchEmail" placeholder="输入邮箱搜索" clearable>
            <template #append>
              <el-button @click="searchUsers">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column label="序号" type="index" width="80"></el-table-column>
        <el-table-column label="用户ID" prop="id" width="100"></el-table-column>
        <el-table-column label="用户邮箱" prop="bemail"></el-table-column>
        <el-table-column label="操作" width="450">
          <template #default="scope">
            <el-button type="primary" size="mini" @click="openEdit(scope.$index)">编辑</el-button>
            <el-button type="danger" size="mini" @click="deleteUser(scope.$index)">删除</el-button>
            <el-button type="warning" size="mini" @click="resetPassword(scope.$index)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <el-pagination 
          background 
          size="small" 
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next" 
          v-model:page-size="pageSize" 
          :page-sizes="[5, 10, 20, 50]"
          v-model:current-page="currentPage" 
          @size-change="handleSizeChange" 
          :total="total" 
          class="mt-4" />
      </div>
    </el-tab-pane>
  </el-tabs>

  <!-- 添加用户对话框 -->
  <el-dialog title="添加用户" v-model="dialogVisible" width="30%">
    <el-form ref="formRef" :rules="rules" :model="form" label-width="80px">
      <el-form-item label="用户邮箱" prop="bemail">
        <el-input v-model="form.bemail" placeholder="请输入用户邮箱"></el-input>
      </el-form-item>
      <el-form-item label="用户密码" prop="bpwd">
        <el-input v-model="form.bpwd" placeholder="请输入密码，为空则使用默认密码" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addUser(formRef)">添加</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 编辑用户对话框 -->
  <el-dialog title="编辑用户" v-model="dialogVisibleEdit" width="30%">
    <el-form ref="formRefEdit" :rules="rules" :model="formEdit" label-width="80px">
      <el-form-item label="用户ID" prop="id">
        <el-input v-model="formEdit.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户邮箱" prop="bemail">
        <el-input v-model="formEdit.bemail" placeholder="请输入用户邮箱"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="editUser(formRefEdit)">修改</el-button>
        <el-button @click="dialogVisibleEdit = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '../axios/request.js'

// 分页参数
const currentPage = ref(1)
const pageSize = ref(5)
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const searchEmail = ref('')

// 对话框控制
const dialogVisible = ref(false)
const dialogVisibleEdit = ref(false)
const form = reactive({})
const formRef = reactive({})
const formEdit = reactive({})
const formRefEdit = reactive({})

// 表单校验规则
const rules = reactive({
  bemail: [
    { required: true, message: '请输入用户邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ]
})

// 初始化加载数据
onMounted(() => {
  loadData()
})

// 加载用户数据
const loadData = () => {
  loading.value = true
  request.myPost('/api/before/bUser/getUsersByPage', {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    bemail: searchEmail.value
  }).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.resData.users
      total.value = res.data.resData.total
    } else {
      ElMessage.error(res.data.msg || '获取用户数据失败')
    }
    loading.value = false
  }).catch(err => {
    console.error('获取用户数据出错:', err)
    ElMessage.error('获取用户数据出错')
    loading.value = false
  })
}

// 搜索用户
const searchUsers = () => {
  currentPage.value = 1
  loadData()
}

// 分页处理
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}

// 打开添加用户对话框
const openAdd = () => {
  dialogVisible.value = true
  form.bemail = ''
  form.bpwd = ''
}

// 添加用户
const addUser = (formRef) => {
  formRef.validate((valid) => {
    if (valid) {
      request.myPost('/api/before/bUser/addUser', form).then(res => {
        if (res.data.code === 200) {
          dialogVisible.value = false
          ElMessage.success('添加用户成功')
          loadData()
        } else {
          ElMessage.error(res.data.msg || '添加用户失败')
        }
      }).catch(err => {
        console.error('添加用户出错:', err)
        ElMessage.error('添加用户出错')
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}

// 打开编辑用户对话框
const openEdit = (index) => {
  dialogVisibleEdit.value = true
  formEdit.id = tableData.value[index].id
  formEdit.bemail = tableData.value[index].bemail
}

// 编辑用户
const editUser = (formRefEdit) => {
  formRefEdit.validate((valid) => {
    if (valid) {
      request.myPost('/api/before/bUser/updateUser', formEdit).then(res => {
        if (res.data.code === 200) {
          dialogVisibleEdit.value = false
          ElMessage.success('修改用户成功')
          loadData()
        } else {
          ElMessage.error(res.data.msg || '修改用户失败')
        }
      }).catch(err => {
        console.error('修改用户出错:', err)
        ElMessage.error('修改用户出错')
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}

// 删除用户
const deleteUser = (index) => {
  ElMessageBox.confirm(
    '确认删除该用户吗？\n此操作将同时删除该用户的所有关联数据，包括：\n- 关注记录\n- 购物车\n- 订单信息\n此操作不可逆，请谨慎操作！',
    '警告',
    {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'warning',
      dangerouslyUseHTMLString: true
    }
  )
  .then(() => {
    const id = tableData.value[index].id
    loading.value = true
    request.myPost('/api/before/bUser/deleteUser', { id }).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('删除用户成功')
        // 如果当前页没有数据了，回到上一页
        if (tableData.value.length === 1 && currentPage.value > 1) {
          currentPage.value--
        }
        loadData()
      } else {
        ElMessage.error(res.data.msg || '删除用户失败')
      }
      loading.value = false
    }).catch(err => {
      console.error('删除用户出错:', err)
      ElMessage.error('删除用户出错: ' + (err.response?.data?.msg || err.message || '未知错误'))
      loading.value = false
    })
  })
  .catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除操作',
    })
  })
}

// 重置密码
const resetPassword = (index) => {
  ElMessageBox.confirm(
    '确认将密码重置为默认密码(123456)吗？',
    '警告',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(() => {
    const user = {
      id: tableData.value[index].id,
      bemail: tableData.value[index].bemail,
      bpwd: '123456' // 默认密码
    }
    request.myPost('/api/before/bUser/updateUser', user).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('密码重置成功')
      } else {
        ElMessage.error(res.data.msg || '密码重置失败')
      }
    }).catch(err => {
      console.error('密码重置出错:', err)
      ElMessage.error('密码重置出错')
    })
  })
  .catch(() => {
    ElMessage({
      type: 'info',
      message: '取消重置密码',
    })
  })
}
</script>

<style scoped>
.mt-4 {
  margin-top: 16px;
}
.el-pagination {
  display: flex;
  justify-content: flex-end;
}
</style> 