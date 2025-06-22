<template>
  <el-tabs type="border-card">
    <el-tab-pane label="商品类型">
      <div style="margin-top: -10px;margin-bottom: 10px;">
        <el-button size="medium" type="success" @click="openadd()">增加</el-button>
      </div>
      <el-table :data="tableData" border style="width: 50%" :key="itemkey">
        <el-table-column label="序号" type="index" width="80"></el-table-column>
        <el-table-column label="类型编号" prop="id" width="100"></el-table-column>
        <el-table-column label="类型名称" prop="typename" width="150"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="mini" @click="openedit(scope.$index)">编辑</el-button>
            <el-button type="warning" size="mini" @click="deleteType(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <el-pagination background size="small" @current-change="handleCurrentChange"
          layout="total,sizes, prev, pager, next" v-model:page-size="pageSize" :page-sizes="[2, 5, 10, 15]"
          v-model:currentPage="currentPage" @size-change="handleSizeChange" :total="total" class="mt-4" />
      </div>
    </el-tab-pane>
  </el-tabs>

  <el-dialog title="增加类型" v-model="dialogVisible" width="30%">
    <el-form ref="formRef" :rules="rules" :model="form" label-width="80px">
      <el-form-item label="类型名称" prop="typename">
        <el-input v-model="form.typename" placeholder="请输入类型名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addType(formRef)">增加</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <el-dialog title="编辑类型" v-model="dialogVisibleEdit" width="30%">
    <el-form ref="formRefEdit" :rules="rules" :model="formEdit" label-width="80px">
      <el-form-item label="类型编号" prop="id">
        <el-input v-model="formEdit.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="类型名称" prop="typename">
        <el-input v-model="formEdit.typename" placeholder="请输入类型名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="editType(formRefEdit)">修改</el-button>
        <el-button @click="dialogVisibleEdit = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage,ElMessageBox } from 'element-plus'
import request from '../axios/request.js'
import { onMounted } from 'vue'

const currentPage = ref(1)
const pageSize = ref(2)
const tableData = ref([])
const itemkey = ref(0)
const total = ref(0)

const dialogVisible = ref(false)
const dialogVisibleEdit = ref(false)
const form = reactive({})
const formRef = reactive({})
const formEdit = reactive({})
const formRefEdit = reactive({})
const rules = reactive({
  typename: [
    { required: true, message: '请输入类型名称', trigger: 'blur' }
  ]
})
//增加类型
const addType = (formRef) => {
  formRef.validate((valid) => {
    if (valid) {
      request.myPost('/api/addType', form).then(res => {
        if (res.data.code === 200) {
          dialogVisible.value = false
          ElMessage.success('添加类型成功')
          loadData()
        }
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}
//打开添加对话框
const openadd = () => {
  dialogVisible.value = true
  form.typename = ''
}
onMounted(() => {
  loadData()
})
//页码变换
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadData()
}
//每页条数变换
const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}
const loadData = () => {
  request.myPost('/api/types', {
    pageNum: currentPage.value,
    pageSize: pageSize.value
  }).then(res => {
    tableData.value = res.data.resData.list
    itemkey.value = Math.random()
    total.value = res.data.resData.total
  })
}
//打开编辑对话框
const openedit = (index) => {
  dialogVisibleEdit.value = true
  formEdit.id = tableData.value[index].id
  formEdit.typename = tableData.value[index].typename
}
//编辑类型
const editType = (formRefEdit) => {
  formRefEdit.validate((valid) => {
    if (valid) {
      request.myPost('/api/editType', formEdit).then(res => {
        if (res.data.code === 200) {
          dialogVisibleEdit.value = false
          ElMessage.success('修改类型成功')
          loadData()
        }
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}
//删除类型
const deleteType = (index) => {
  ElMessageBox.confirm(
    '确认删除该类型吗？',
    '警告',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(() => {
    let id = tableData.value[index].id
    request.myPost('/api/deleteType', { id: id }).then(res => {
      if (res.data.code === 200) {
        loadData()
        ElMessage.success({
          message: '删除成功'
        })
      } else {
        if(res.data.code === 303){
          ElMessage.error({
            message: res.data.msg
          })
        }else{
          ElMessage.error({
            message: '删除失败'
          })
        }
      }
    }).catch(err => {
      console.log(err)
    })
  })
  .catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}
</script>

<style>

</style>
