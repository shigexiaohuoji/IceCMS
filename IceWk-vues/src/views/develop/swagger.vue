<template>
  <div>
    <iframe :src="url" frameborder="0" height="600" scrolling="yes" width="100%"></iframe>
  </div>
</template>

<script>
const baseUrl = process.env.VUE_APP_BASE_API;
export default {
  name: 'Swagger',
  components: {},
  props: {},
  data() {
    return {
      url: baseUrl + '/doc.html'
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {},
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {}
};
</script>
