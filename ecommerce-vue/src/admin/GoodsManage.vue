<template>
  <el-tabs type="border-card">
    <el-tab-pane label="商品管理">
      <div style="margin-top: -10px;margin-bottom: 10px;" >
        <el-button size="medium" type="success" @click="openadd()">增加</el-button>
        <el-form inline>
          <el-form-item label="商品名称">
            <el-input v-model="goodsName"></el-input>
          </el-form-item>
           <el-form-item label="商品类型">
                <el-select v-model="goodsType" placeholder="商品类型" style="width: 150px;">
                    <el-option label="---" value="0"></el-option>
                    <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
                </el-select>
		      </el-form-item>
          <el-form-item>
            <el-button type="primary" size="medium" @click="loadData()">搜索</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="tableData" border style="width: 100%" :key="itemkey">
        <el-table-column label="序号" type="index" width="80"></el-table-column>
        <el-table-column label="商品编号" prop="id" width="100"></el-table-column>
        <el-table-column label="商品名称" prop="gname" width="150"></el-table-column>
        <el-table-column label="商品类型" prop="typename" width="150"></el-table-column>
        <el-table-column label="商品图片" prop="gpicture" width="150">
          <template #default="scope">
            <el-image
                v-if="scope.row.gpicture"
                :src=getAssetsFile(scope.row.gpicture)
                style="width: 60px; height: 60px; border-radius: 4px;">
              <template #error>
                <div class="image-error">加载失败</div>
              </template>
            </el-image>
            <span v-else>无图片</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="mini" @click="openedit(scope.$index)">编辑</el-button>
            <el-button type="warning" size="mini" @click="deleteGoods(scope.$index)">删除</el-button>
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

  <el-dialog title="增加商品" v-model="dialogVisible" width="50%">
    <el-form ref="addFormRef" :model="addForm" :rules="rules" label-width="80px">
      <el-form-item label="商品名称" prop="gname">
        <el-input v-model="addForm.gname"></el-input>
      </el-form-item>
      <el-form-item label="商品原价" prop="goprice">
        <el-input v-model="addForm.goprice"></el-input>
      </el-form-item>
      <el-form-item label="商品现价" prop="grprice">
        <el-input v-model="addForm.grprice"></el-input>
      </el-form-item>
      <el-form-item label="商品库存" prop="gstore">
        <el-input v-model="addForm.gstore"></el-input>
      </el-form-item>
      <el-form-item label="是否广告">
        <el-radio-group v-model="addForm.isAdvertisement">
          <el-radio label="1" size="large">是</el-radio>
          <el-radio label="2" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品类型" prop="goodstypeId">
        <el-select v-model="addForm.goodstypeId" placeholder="商品类型">
          <el-option label="---" value="0"></el-option>
          <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="商品图片" prop="gpicture">
          <el-upload
              v-model:file-list="fileList"
              class="upload-demo"
              action="/api/goods/upload"
              :before-upload = "beforeUpload"
              list-type="picture"
              name="file"
          >
            <el-button type="primary">上传图片</el-button>
            <template #tip>
              <div class="el-upload__tip">
                jpg/png 文件的大小不超过500kb
              </div>
            </template>
          </el-upload>
        </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="addGoods(addFormRef)">添加</el-button>
        <el-button @click="resetForm" type="warning">重置</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog title="修改商品" v-model="updateDialogVisible" width="50%">
    <el-form ref="updateFormRef" :model="updateForm" :rules="rules" label-width="80px">
      <el-form-item label="商品ID" prop="id">
        <el-input v-model="updateForm.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="商品名称" prop="gname">
        <el-input v-model="updateForm.gname"></el-input>
      </el-form-item>
      <el-form-item label="商品原价" prop="goprice">
        <el-input v-model="updateForm.goprice"></el-input>
      </el-form-item>
      <el-form-item label="商品现价" prop="grprice">
        <el-input v-model="updateForm.grprice"></el-input>
      </el-form-item>
      <el-form-item label="商品库存" prop="gstore">
        <el-input v-model="updateForm.gstore"></el-input>
      </el-form-item>
      <el-form-item label="是否广告">
        <el-radio-group v-model="updateForm.isAdvertisement">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="2" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品类型" prop="goodstypeId">
        <el-select v-model="updateForm.goodstypeId" placeholder="商品类型">
          <el-option label="---" value="0"></el-option>
          <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="商品图片" prop="gpicture">
          <el-upload
              v-model:file-list="fileList"
              class="upload-demo"
              action="/api/goods/upload"
              :before-upload = "beforeUpload"
              list-type="picture"
              name="file"
          >
            <el-button type="primary">上传图片</el-button>
            <template #tip>
              <div class="el-upload__tip">
                jpg/png 文件的大小不超过500kb
              </div>
            </template>
          </el-upload>
        </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="updateGoods(updateFormRef)">修改</el-button>
        <el-button @click="resetUpdateForm" type="warning">重置</el-button>
      </div>
    </template>
  </el-dialog>


</template>

<script setup>
import { onMounted, reactive, ref } from "vue"
//引入axios实例
import axios from '../axios/request.js'
import { ElMessage, ElMessageBox } from 'element-plus'
const dialogVisible = ref(false)
const updateDialogVisible = ref(false)
const updateForm = reactive({})
const updateFormRef = ref(null)
const addForm= reactive({
    isAdvertisement: '2'
})
const addFormRef = ref(null)
const tableData = ref([])
const itemkey = ref(0)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(2)
const goodsName = ref('')
const goodsType = ref('')
const fileList = ref([])
const typeoptions = ref([])
//验证规则
const rules = reactive({
    gname: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
    goprice: [{ required: true, message: '请输入商品原价', trigger: 'blur' }],
    grprice: [{ required: true, message: '请输入商品折扣价', trigger: 'blur' }],
    gstore: [{ required: true, message: '请输入商品库存', trigger: 'blur' }],
    goodstypeId: [{ required: true, message: '请选择商品类型', trigger: 'change' }]
})
// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt500kb = file.size / 1024 <= 5000
  if (!isJPG && !isPNG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt500kb) {
    ElMessage.error('上传图片大小不能超过 5000kb!')
  }
  if(fileList.value.length > 0){
    ElMessage.error('一次只能上传一张图片!')
    return false
  }
  return isJPG || isPNG && isLt500kb
}
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
//加载页面数据
onMounted(() => {
  getTypeOptions()
  loadData()
})
//获取商品类型
const getTypeOptions = () => {
  axios.myGet('/api/goods/getGoodsType').then(res => {
    typeoptions.value = res.data.resData
  }).catch(err => {
    console.log(err)
  })
}

//加载数据
const loadData = () => {
  //get请求没有请求体，需要在后端使用@ModelAttribute注解将参数注入到方法中
  axios.myPost('/api/goods/goodsList',
    {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      gname: goodsName.value,
      goodstypeId: goodsType.value
    }).then(res => {
      tableData.value = res.data.resData.goodsList
      itemkey.value = Math.random()
      total.value = res.data.resData.total
    }).catch(err => {
      console.log(err)
    })
}
//打开添加客户弹窗
const openadd = () => {
  dialogVisible.value = true
}
//处理添加客户
const addGoods = (formRef) => {
   formRef.validate((valid) => {
    if (valid) {
      axios.myPost('/api/goods/addGoods', addForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success({
            message: '添加成功'
          })
          dialogVisible.value = false
          //重置表单
          addFormRef.value.resetFields()
          loadData()
        } else {
          alert("添加失败")
        }
      }).catch(err => {
        console.log(err)
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}
//重置表单
const resetForm = () => {
  addFormRef.value.resetFields()
}
//打开修改客户弹窗
const openedit = (index) => {
  updateDialogVisible.value = true
  updateForm.id = tableData.value[index].id
  updateForm.gname = tableData.value[index].gname
  updateForm.goprice = tableData.value[index].goprice
  updateForm.grprice = tableData.value[index].grprice
  updateForm.gstore = tableData.value[index].gstore
  updateForm.isAdvertisement = tableData.value[index].isAdvertisement
  updateForm.goodstypeId = tableData.value[index].goodstypeId
  if(tableData.value[index].gpicture){
    fileList.value = [{
      name: tableData.value[index].gpicture,
      url: getAssetsFile(tableData.value[index].gpicture)
    }]
  }
}
//处理修改客户
const updateGoods = (formRef) => {
   formRef.validate((valid) => {
    if (valid) {
      axios.myPost('/api/goods/updateGoods', updateForm).then(res => {
        if (res.data.code === 200) {
          loadData()
          ElMessage.success({
            message: '修改成功'
          })
          updateDialogVisible.value = false
          //重置表单
          updateFormRef.value.resetFields()
        } else {
          alert("修改失败")
        }
      }).catch(err => {
        console.log(err)
      })
    } else {
      ElMessage.error('表单验证失败')
    }
   })
}
//重置修改表单
const resetUpdateForm = () => {
  updateFormRef.value.resetFields()
}
//删除客户
const deleteGoods = (index) => {
  ElMessageBox.confirm(
    '确认删除该商品吗？',
    '警告',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    let id = tableData.value[index].id
    axios.myPost('/api/goods/deleteGoods', { id: id }).then(res => {
      if (res.data.code === 304) {
        ElMessage.error({
          message: res.data.msg
        })
      } else {
          if (res.data.code === 200) {
            loadData()
            ElMessage.success({
              message: '删除成功'
            })
          } else {
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

<style></style>