<template>
     <el-dialog title="我的购物车" v-model="myfocusVisible" width="50%" @close="goClose(1)">
      <div v-if="!isLoggedIn" class="login-reminder">
        <p>您尚未登录，请先登录账号再查看购物车</p>
        <el-button type="primary" @click="goToLogin">去登录</el-button>
      </div>
      <el-table v-else ref="tableRef" :data="goodslists" border :key="itemKey" @selection-change="handleSelectionChange" row-key="cid">
        <el-table-column type="selection" width="55" :reserve-selection="true" />
        <el-table-column label="图片">
          <template #default="scope">
            <el-link @click="goToGoodsDetail(scope.row)">
                <el-image :src="getAssetsFile(scope.row.gpicture)" style="width: 50px; height: 50px;"/>
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="商品名称">
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
        <el-table-column label="购买量">
            <template #default="scope">
                <span>{{scope.row.shoppingnum}}</span>
             </template>
        </el-table-column>
        <el-table-column label="小计">
             <template #default="scope">
                <span>{{(scope.row.grprice * scope.row.shoppingnum).toFixed(1)}}</span>
            </template>
        </el-table-column>
        <el-table-column label="删除">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="danger" :icon="Delete" circle @click="remove(scope.row)"/>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
      <br>
      <div v-if="isLoggedIn && goodslists.length > 0">
        已选择商品总价：¥ {{ selectedTotalPrice.toFixed(1) }} &nbsp;
        <el-button type="success" :icon="ShoppingBag" @click="goClose(2)" :disabled="selectedItems.length === 0">去结算</el-button>
        <el-button type="primary" @click="selectAll">全选</el-button>
        <el-button type="info" @click="unselectAll">取消全选</el-button>
        <el-button type="danger" :icon="Delete" @click="removeAll">清空购物车</el-button>
      </div>
      <div v-else-if="isLoggedIn && goodslists.length === 0" class="empty-cart">
        <p>您的购物车还是空的，快去选购喜欢的玩偶吧！</p>
        <el-button type="primary" @click="goToIndex">去购物</el-button>
      </div>
  </el-dialog>
</template>
<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { Delete, ShoppingBag } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from '../axios/request.js'
const router = useRouter()
const route = useRoute() 
const myfocusVisible = ref(true) 
const goodslists = ref([])
let itemKey = ref(0)
const selectedItems = ref([])
const tableRef = ref(null)
const isUpdatingSelection = ref(false)
const isLoggedIn = ref(false)
const userId = ref(null)

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

// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}

//加载商品信息
const loadGoods = () => {
  if (!isLoggedIn.value) {
    goodslists.value = []
    return
  }
  
  axios.myPost('/api/before/cart/myCart',
  {
    busertableId: userId.value
  })
  .then(res => {
      goodslists.value = res.data.resData || [];
      
      // 设置商品的选中状态
      goodslists.value.forEach(item => {
        if (item.selected === undefined || item.selected === null) {
          item.selected = 1; // 默认选中
        }
      });
      
      // 更新itemKey触发表格重新渲染
      itemKey.value = Date.now();
      
      // 更新选中的商品列表
      selectedItems.value = goodslists.value.filter(item => item.selected === 1);
      
      // 在表格渲染完成后设置选中状态
      nextTick(() => {
        setTableSelection();
      });
  })
  .catch((error) => {
      ElMessage.error('获取购物车数据失败，请稍后再试')
      console.error(error)
  })
}

// 跳转到登录页面
const goToLogin = () => {
  router.push({
    name: 'login',
    query: { redirect: route.fullPath }
  })
}

// 跳转到首页
const goToIndex = () => {
  router.push({ name: 'index' })
}

// 设置表格选中状态
const setTableSelection = () => {
  if (!tableRef.value) return;
  
  // 防止触发handleSelectionChange
  isUpdatingSelection.value = true;
  
  // 先清除所有选择
  tableRef.value.clearSelection();
  
  // 然后为每个selected=1的行设置选中状态
  goodslists.value.forEach((row) => {
    if (row.selected === 1) {
      tableRef.value.toggleRowSelection(row, true);
    }
  });
  
  // 恢复handleSelectionChange的触发
  setTimeout(() => {
    isUpdatingSelection.value = false;
  }, 0);
}

const goToGoodsDetail = (goods) => {
    router.push({name: 'goodsDetail', state: goods })
}

const goClose = (n) => {
    // 如果未登录，直接返回
    if (!isLoggedIn.value) {
      let path = route.query.redirect
      router.replace({ path: path === '/' || path === undefined ? '/' : path })
      return
    }
    
    //修改完购物车后，关闭对话框时批量更新
    let cids = []
    let shoppingnums = []
    let selecteds = []
    for (let i = 0; i < goodslists.value.length; i++) {
        let item = goodslists.value[i]
        cids[i] = item.cid
        shoppingnums[i] = item.shoppingnum
        selecteds[i] = item.selected
    }
    
    if (goodslists.value.length > 0) {
      axios.myPost('/api/before/cart/bupDateCart',
      {
        bcid: cids,
        bshoppingnum: shoppingnums,
        bselected: selecteds
      }).catch(error => {
        ElMessage.error('更新购物车失败')
        console.error(error)
      })
    }
    
    //跳转到前一个页面
    let path = route.query.redirect
    if(n === 1) { //关闭
      router.replace({ path: path === '/' || path === undefined ? '/' : path })
    } else { //去结算
      if (selectedItems.value.length === 0) {
        ElMessage.warning('请至少选择一件商品')
        return
      }
      router.push({name: 'goOrder'})
    }
}

//删除购物车
const remove = (goods) => {
  axios.myPost('/api/before/cart/removeCart',
    {
      id: goods.cid
    }).then(res => {
      ElMessage.success({message: '成功删除购物车！',type: 'success'})
      loadGoods()
    })
}

// 处理选择变化
const handleSelectionChange = (selection) => {
  if (isUpdatingSelection.value) return;
  
  selectedItems.value = selection;
  
  // 更新商品的选中状态
  goodslists.value.forEach(item => {
    const isSelected = selection.some(selected => selected.cid === item.cid);
    item.selected = isSelected ? 1 : 0;
  });
}

// 全选
const selectAll = () => {
  // 更新数据模型
  goodslists.value.forEach(item => {
    item.selected = 1;
  });
  
  // 更新选中的商品列表
  selectedItems.value = [...goodslists.value];
  
  // 设置表格选中状态
  setTableSelection();
}

// 取消全选
const unselectAll = () => {
  // 更新数据模型
  goodslists.value.forEach(item => {
    item.selected = 0;
  });
  
  // 更新选中的商品列表
  selectedItems.value = [];
  
  // 清除表格选中状态
  if (tableRef.value) {
    tableRef.value.clearSelection();
  }
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

// 计算已选择商品的总价
const selectedTotalPrice = computed(() => {
    let total = 0
    for (let i = 0; i < selectedItems.value.length; i++) {
        let item = selectedItems.value[i]
        total = total + item.grprice * item.shoppingnum
    }
    return total
})

//清空购物车
const removeAll = () => {
  axios.myPost('/api/before/cart/clearCart',
    {
      busertableId: sessionStorage.getItem('bid')
    }).then(res => {
      ElMessage.success({message: '已清空购物车！',type: 'success'})
      loadGoods()
    })
}

// 监听goodslists变化，确保表格选择状态与数据模型一致
watch(goodslists, () => {
  nextTick(() => {
    setTableSelection();
  });
}, { deep: true });
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

.empty-cart {
  text-align: center;
  padding: 30px;
}

.empty-cart p {
  color: var(--doll-text);
  font-size: 16px;
  margin-bottom: 20px;
}

/* Add your other styles here */
</style>