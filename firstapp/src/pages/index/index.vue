<template>
  <div @click="clickHandle" id="home">
    <div class="userinfo" @click="bindViewTap">
      <img
        class="userinfo-avatar"
        v-if="userInfo.avatarUrl"
        :src="userInfo.avatarUrl"
        background-size="cover"
      >
      <img class="userinfo-avatar" src="/static/images/user.png" background-size="cover">

      <div class="userinfo-nickname">
        <card :text="userInfo.nickName"></card>
      </div>
    </div>

    <div class="usermotto">
      <div class="user-motto">
        <card :text="motto"></card>
      </div>
    </div>

    <form class="form-container">
      <input type="text" class="form-control" :value="motto" placeholder="v-model">
      <input type="text" class="form-control" v-model="motto" placeholder="v-model">
      <input type="text" class="form-control" v-model.lazy="motto" placeholder="v-model.lazy">
    </form>

    <a href="/pages/counter/main" class="counter">去往Vuex示例页面</a>

    <div class="all">
      <div class="left"></div>
      <div class="right"></div>
    </div>

    <van-button>测试</van-button>
    <van-button type="primary" @click="clickButtom">主要按钮</van-button>

    <van-action-sheet :show="show" :actions="actions" cancel-text="取消" @cancel="onClose"/>

    <van-field
      :value="sms"
      type="password"
      clearable
      left-icon="contact"
      label="用户名"
      @change="inputChange"
      placeholder="请输入短信验证码"
    >
      <van-button slot="button" size="small" type="primary" @click="doClick" custom-class="login-btn">发送验证码</van-button>
    </van-field>

  <van-field
    value="输入框已禁用"
    label="用户名"
    left-icon="contact"
    disabled
  />

  </div>
</template>

<script>
import card from "@/components/card";

export default {
  data() {
    return {
      sms: "",
      show: false,
      actions: [
        {
          name: "选项"
        },
        {
          name: "分享",
          subname: "描述信息",
          openType: "share"
        },
        {
          loading: true
        },
        {
          name: "禁用选项",
          disabled: true
        }
      ],
      motto: "Hello miniprograme",
      userInfo: {
        nickName: "mpvue",
        avatarUrl: "http://mpvue.com/assets/logo.png"
      }
    };
  },

  components: {
    card
  },

  methods: {
    bindViewTap() {
      const url = "../logs/main";
      if (mpvuePlatform === "wx") {
        mpvue.switchTab({ url });
      } else {
        mpvue.navigateTo({ url });
      }
    },
    clickButtom() {
      this.show = true;
    },
    onClose() {
      console.log("点击了取消");
      this.show = false;
    },
    async doClick() {
      console.log(this.sms);
      let res = await this.$post('reqDoneRate/queryDetailData',{
        startTime: "2019-04-01",
        endTime: "2019-04-28"
      });
      console.log(res);
    },
    inputChange(val) {  
      this.sms = val.mp.detail;
    }
  },

  created() {
    // let app = getApp()
  }
};
</script>

<style scoped>
.userinfo {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userinfo-avatar {
  width: 128rpx;
  height: 128rpx;
  margin: 20rpx;
  border-radius: 50%;
}

.userinfo-nickname {
  color: #aaa;
}

.usermotto {
  margin-top: 150px;
}

.form-control {
  display: block;
  padding: 0 12px;
  margin-bottom: 5px;
  border: 1px solid #ccc;
}
.all {
  width: 7.5rem;
  height: 1rem;
  background-color: blue;
}
.all:after {
  display: block;
  content: "";
  clear: both;
}
.left {
  float: left;
  width: 3rem;
  height: 1rem;
  background-color: red;
}

.right {
  float: left;
  width: 4.5rem;
  height: 1rem;
  background-color: green;
}
</style>
<style>
#home .login-btn {
  background-color: #ee3f4d!important;
  border: 1px solid #ee3f4d!important;
}
#home .van-cell {
  align-items: center;
}
</style>
