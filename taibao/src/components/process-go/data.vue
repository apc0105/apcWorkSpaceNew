<script>
  export default {
    data(){
      return {
        User: {
          category: "User",
          shape: "Ellipse",
          shapeOptions: {
            fill: "bluegrad",
            portId: "",
            cursor: "pointer",
            fromEndSegmentLength: 40
          }
        },
        Supplier: {
          category: "Supplier",
          shape: "Ellipse",
          shapeOptions: {
            fill: "greengrad",
            portId: "",
            toEndSegmentLength: 40
          }
        },

        myjson: {
          "copiesArrays": true,
          "copiesArrayObjects": true,
          "nodeDataArray": [
//            {"key": -1, "category": "User", "text": "用户提交资料"},
//
//            {"key": 0, "text": "系统交付出错"},
//
//            {
//              "key": 100, "category": "Supplier", "text": "供应商驳回",
//              "reasonsList": [{"text": "内容描述不具体"}]
//            },
//
//            {"key": 101, "category": "Supplier", "text": "供应商接单"},
//            {"key": 102, "category": "Supplier", "text": "供应商完工"},
//            {"key": 103, "category": "Supplier", "text": "等待供应商处理"},
//            {"key": 104, "category": "Supplier", "text": "供应商接单"},
//            {"key": 105, "category": "Supplier", "text": "供应商完工"},
//            {"key": 106, "category": "Supplier", "text": "等待供应商处理"},
//            {"key": 107, "category": "Supplier", "text": "供应商接单"},
//            {"key": 108, "category": "Supplier", "text": "供应商完工"},
//            {"key": 109, "category": "Supplier", "text": "等待供应商处理"},
//            {"key": 110, "category": "Supplier", "text": "供应商接单"},
//            {"key": 111, "category": "Supplier", "text": "供应商完工"},
//            {"key": 112, "category": "Supplier", "text": "等待供应商处理"}

          ],
          "linkDataArray": [
/*
            {"from": -1, "to": 101},
            {"from": 101, "to": 102},
            {"from": 102, "to": 0},
            {"from": 0, "to": 103},
            {"from": -3, "to": 103},
            {"from": 102, "to": -3},
            {"from": 103, "to": 100},

            {"from": 103, "to": 104},
            {"from": 104, "to": 105},
            {"from": 105, "to": 106},
            {"from": 106, "to": 107},
            {"from": 107, "to": 108},
            {"from": 108, "to": 109},
            {"from": 110, "to": 111},
            {"from": 111, "to": 112}*/
          ]
        }
      }
    }
  }
</script>

