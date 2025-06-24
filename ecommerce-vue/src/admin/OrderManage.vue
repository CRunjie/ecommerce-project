<template>
  <el-tabs type="border-card">
    <el-tab-pane label="订单管理">
      <div style="margin: 10px 0;">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="订单金额">
            <div class="input-range">
              <el-input v-model="searchForm.minAmount" placeholder="最小金额"></el-input>
              <span style="margin: 0 5px;">-</span>
              <el-input v-model="searchForm.maxAmount" placeholder="最大金额"></el-input>
            </div>
          </el-form-item>
          <el-form-item label="订单号">
            <div class="input-range">
              <el-input v-model="searchForm.minId" placeholder="最小订单号"></el-input>
              <span style="margin: 0 5px;">-</span>
              <el-input v-model="searchForm.maxId" placeholder="最大订单号"></el-input>
            </div>
          </el-form-item>
          <el-form-item label="下单时间">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="支付状态">
            <el-select v-model="searchForm.status" placeholder="全部" style="width: 120px;">
              <el-option label="全部" :value="null" />
              <el-option label="未支付" :value="0" />
              <el-option label="已支付" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="删除状态">
            <el-select v-model="searchForm.isDeleted" placeholder="全部" style="width: 120px;">
              <el-option label="全部" :value="null" />
              <el-option label="未删除" :value="0" />
              <el-option label="已删除" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="包含商品">
            <el-select v-model="searchForm.goodstableId" placeholder="选择商品" clearable filterable style="width: 220px;">
              <el-option label="全部商品" :value="null" />
              <el-option 
                v-for="item in goodsOptions" 
                :key="item.id" 
                :label="item.name" 
                :value="item.id">
                <div class="goods-option">
                  <span>{{ item.name }}</span>
                  <span class="goods-price">¥{{ item.price.toFixed(2) }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchOrders">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column label="序号" type="index" width="60"></el-table-column>
        <el-table-column label="订单号" prop="id" width="80"></el-table-column>
        <el-table-column label="用户ID" prop="busertableId" width="80"></el-table-column>
        <el-table-column label="订单金额" width="100">
          <template #default="scope">
            <span style="color: #f56c6c;">¥{{ scope.row.amount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" width="180">
          <template #default="scope">
            {{ scope.row.orderdate }}
          </template>
        </el-table-column>
        <el-table-column label="订单状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '已支付' : '未支付' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户可见" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isDeleted === 1 ? 'danger' : 'info'">
              {{ scope.row.isDeleted === 1 ? '已删除' : '可见' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewOrderDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-tab-pane>
  </el-tabs>
  
  <!-- 订单详情对话框 -->
  <el-dialog title="订单详情" v-model="dialogVisible" width="650px">
    <div class="order-info">
      <p><strong>订单号：</strong>{{ currentOrder.id }}</p>
      <p><strong>用户ID：</strong>{{ currentOrder.busertableId }}</p>
      <p><strong>订单金额：</strong>¥{{ currentOrder.amount?.toFixed(2) }}</p>
      <p><strong>下单时间：</strong>{{ currentOrder.orderdate }}</p>
      <p><strong>订单状态：</strong>
        <el-tag :type="currentOrder.status === 1 ? 'success' : 'warning'">
          {{ currentOrder.status === 1 ? '已支付' : '未支付' }}
        </el-tag>
      </p>
      <p><strong>用户可见状态：</strong>
        <el-tag :type="currentOrder.isDeleted === 1 ? 'danger' : 'info'">
          {{ currentOrder.isDeleted === 1 ? '用户已删除（仅管理员可见）' : '用户可见' }}
        </el-tag>
      </p>
    </div>
    
    <div class="order-items" v-if="orderDetails.length > 0">
      <h3>订单商品</h3>
      <el-table :data="orderDetails" border>
        <el-table-column label="商品ID" prop="id" width="80"></el-table-column>
        <el-table-column label="商品名称" prop="gname"></el-table-column>
        <el-table-column label="单价" width="100">
          <template #default="scope">
            ¥{{ scope.row.grprice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="数量" prop="shoppingnum" width="80"></el-table-column>
        <el-table-column label="小计" width="100">
          <template #default="scope">
            ¥{{ scope.row.smallTotal.toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../axios/request.js'

// 分页参数
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 商品选项数据
const goodsOptions = ref([])

// 搜索表单
const searchForm = reactive({
  minAmount: '',
  maxAmount: '',
  minId: '',
  maxId: '',
  startDate: '',
  endDate: '',
  status: null,
  isDeleted: null,
  goodstableId: null,
})

// 日期范围选择器
const dateRange = ref([])

// 订单详情
const dialogVisible = ref(false)
const currentOrder = ref({})
const orderDetails = ref([])

// 初始化加载数据
onMounted(() => {
  loadOrderData()
  loadGoodsOptions()
})

// 加载商品选项
const loadGoodsOptions = () => {
  request.myGet('/api/goods/getAllGoodsForSelect')
    .then(res => {
      if (res.data.code === 200) {
        goodsOptions.value = res.data.resData
      } else {
        console.error('获取商品列表失败:', res.data)
        ElMessage.error('获取商品列表失败')
      }
    })
    .catch(err => {
      console.error('获取商品列表出错:', err)
      ElMessage.error('获取商品列表出错')
    })
}

// 加载订单数据
const loadOrderData = () => {
  loading.value = true
  
  // 处理日期范围
  if (dateRange.value && dateRange.value.length === 2) {
    searchForm.startDate = dateRange.value[0]
    searchForm.endDate = dateRange.value[1]
  }
  
  const params = {
    ...searchForm,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  }
  
  // 过滤掉空字符串字段
  Object.keys(params).forEach(key => {
    if (params[key] === '') {
      params[key] = null
    }
  })
  
  // 数值类型参数处理
  if (params.minAmount) params.minAmount = Number(params.minAmount);
  if (params.maxAmount) params.maxAmount = Number(params.maxAmount);
  if (params.minId) params.minId = Number(params.minId);
  if (params.maxId) params.maxId = Number(params.maxId);
  if (params.goodstableId) params.goodstableId = Number(params.goodstableId);
  
  console.log('查询参数:', params); // 添加调试日志
  
  request.myPost('/api/goods/getAllOrders', params)
    .then(res => {
      if (res.data.code === 200) {
        tableData.value = res.data.resData.orders
        total.value = res.data.resData.total
      } else {
        console.error('获取订单数据失败:', res.data)
        ElMessage.error(res.data.msg || '获取订单数据失败')
        tableData.value = []
        total.value = 0
      }
      loading.value = false
    })
    .catch(err => {
      console.error('获取订单数据出错:', err)
      ElMessage.error('获取订单数据出错')
      tableData.value = []
      total.value = 0
      loading.value = false
    })
}

// 查看订单详情
const viewOrderDetail = (order) => {
  currentOrder.value = order
  dialogVisible.value = true
  
  // 获取订单详情
  request.myPost('/api/before/orders/getOrdersDetail', { id: order.id })
    .then(res => {
      if (res.data.code === 200) {
        orderDetails.value = res.data.resData
      } else {
        orderDetails.value = []
        ElMessage.error('获取订单详情失败')
      }
    })
    .catch(err => {
      console.error('获取订单详情出错:', err)
      ElMessage.error('获取订单详情出错')
    })
}

// 搜索订单
const searchOrders = () => {
  currentPage.value = 1  // 重置为第一页
  loadOrderData()
}

// 重置搜索条件
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'status' || key === 'isDeleted' || key === 'goodstableId' ? null : ''
  })
  dateRange.value = []
  currentPage.value = 1
  loadOrderData()
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  loadOrderData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadOrderData()
}
</script>

<style scoped>
.input-range {
  display: flex;
  align-items: center;
  width: 220px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.order-info {
  margin-bottom: 20px;
  padding: 10px;
  border-radius: 4px;
  background-color: #f8f8f8;
}

.order-info p {
  margin: 8px 0;
}

.order-items h3 {
  margin: 15px 0;
}

.goods-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.goods-price {
  color: #f56c6c;
  font-size: 12px;
}

:deep(.el-date-editor.el-input__wrapper) {
  width: 260px;
}
</style> 