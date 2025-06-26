<template>
    <el-dialog title="订单确认" v-model="myfocusVisible" width="50%" @close="goClose()">
     <div v-if="!isLoggedIn" class="login-reminder">
       <p>您尚未登录，请先登录账号再确认订单</p>
       <el-button type="primary" @click="goToLogin">去登录</el-button>
     </div>
     <div v-else>
       <el-table :data="goodslists"  border :key="itemKey" row-key="cid">
         <el-table-column label="图片">
           <template #default="scope">
             <el-link @click="goToGoodsDetail(scope.row)">
                 <el-image  :src="getAssetsFile(scope.row.gpicture)" style="width: 60px; height: 50px;"/>
             </el-link>
           </template>
         </el-table-column>
         <el-table-column  label="玩偶名称">
             <template #default="scope">
                       <el-link @click="goToGoodsDetail(scope.row)" underline="never">
                 <span>{{scope.row.gname}}</span>
             </el-link>
           </template>
         </el-table-column>
         <el-table-column label="商品实价">
           <template #default="scope">
             <span>{{scope.row.grprice.toFixed(1)}}</span>
           </template>
         </el-table-column>
         <el-table-column  label="购买量">
             <template #default="scope">
                 <span>&nbsp;{{scope.row.shoppingnum}}&nbsp;</span>
              </template>
         </el-table-column>
         <el-table-column  label="小计">
              <template #default="scope">
                 <span>{{(scope.row.grprice * scope.row.shoppingnum).toFixed(1)}}</span>
             </template>
         </el-table-column>
       </el-table>
       <br>
       <div v-if="goodslists.length > 0">总价：¥ {{ totalPrice.toFixed(1) }} &nbsp;
         <el-button type="success" :icon="Finished" @click="submitOrder()">提交订单</el-button>
       </div>
       <div v-else class="empty-order">
         <p>没有选中的玩偶，请返回购物车选择心仪的玩偶</p>
         <el-button type="primary" @click="goToCart">返回购物车</el-button>
       </div>
     </div>
 </el-dialog>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { Finished} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from '../axios/request.js'
const router = useRouter()
const route = useRoute() 
const myfocusVisible = ref(true) 
const goodslists = ref([])
let itemKey = ref(0)
const isLoggedIn = ref(false)
const userId = ref(null)

// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}

//组件初始化
onMounted(() => {
 checkLoginStatus()
 loadGoods()
})

// 检查登录状态
const checkLoginStatus = () => {
  const bid = sessionStorage.getItem('bid')
  userId.value = bid
  isLoggedIn.value = bid !== null
}

// 跳转到登录页面
const goToLogin = () => {
  router.push({
    name: 'login',
    query: { redirect: route.fullPath }
  })
}

// 跳转到购物车
const goToCart = () => {
  router.push({
    name: 'mycart'
  })
}

//加载商品信息 - 只加载已选中的商品
const loadGoods = () => {
 if (!isLoggedIn.value) {
   goodslists.value = []
   return
 }
 
 axios.myPost('/api/before/cart/getSelectedGoods',
 {
   busertableId: userId.value
 })
 .then(res => {
     goodslists.value = res.data.resData || [];
     itemKey.value = Date.now(); //刷新表格数据
 })
 .catch((error) => {
     ElMessage.error('获取订单数据失败，请稍后再试')
     console.error(error)
     goClose()
 })
}

const goToGoodsDetail = (goods) => {
   router.push({name: 'goodsDetail', state: goods })
}

const goClose = () => {
   //跳转到前一个页面
   let path = route.query.redirect
   router.replace({ path: path === '/' || path === undefined ? '/' : path })
}

//使用计算属性计算总额
const totalPrice = computed(() => {
   let total = 0
   for (let i = 0; i < goodslists.value.length; i++) {
       let item = goodslists.value[i]
       total = total + item.grprice * item.shoppingnum
   }
   return total
})

const submitOrder = () => {
  // 检查登录状态和购物车商品
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    goToLogin()
    return
  }
  
  // 检查是否有选中的商品
  if (goodslists.value.length === 0) {
    ElMessage.warning('请至少选择一件玩偶')
    return
  }
  
  let gids = []
  let shoppingnums = []
  for (let i = 0; i < goodslists.value.length; i++) {
      let item = goodslists.value[i]
      gids[i] = item.id
      shoppingnums[i] = item.shoppingnum
  }
  
  axios.myPost('/api/before/orders/submitOrder',
  {
    bgid: gids,
    bshoppingnum: shoppingnums,
    busertableId: userId.value,
    amount: totalPrice.value
  }).then(res => {
      if(res.data.resData.id > 0){
        ElMessage.success({message: '订单提交成功，感谢您购买我们的玩偶！',type: 'success'})
        router.push({name: 'index'})
      } else
        ElMessage.error('订单提交失败！')
  })
  .catch((error) => {
      ElMessage.error('提交订单失败，请稍后再试')
      console.error(error)
  })
}
</script>
<style scoped>
.login-reminder {
  text-align: center;
  padding: 30px;
}

.login-reminder p {
  color: var(--doll-secondary);
  font-size: 16px;
  margin-bottom: 20px;
}

.empty-order {
  text-align: center;
  padding: 20px 0;
}

.empty-order p {
  color: var(--doll-text);
  font-size: 16px;
  margin-bottom: 20px;
}

/* Add your other styles here */
</style>