
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import RecipeDetailCard from "../component/recipe-detail-card/component";
import {useParams} from "react-router-dom";


import "../common/index.css";

const RecipeDetailPage = () => {

  const params = useParams();

  const items = [
    {
      recipeId:0,
      mainImageUrl:"/img/data/item0_0.jpg",
      title:"제가 만든 돈가스 맛나요~",
      tags:["돈가스", "양식", "다이어트 파괴자","존맛탱"],
      createdDate:"2023-03-16",
      author:"갱훈",
      commentary:"이 돈가스를 먹고 나서 제 인생이 달라졌습니다 여러분도 만들어서 드셔보십쇼.",
      steps:[
        {
          order:0,
          screenshotUrl:"/img/data/item0_1.jpg",
          detail:"이런 재료들을 준비해주세요"
        },
        {
          order:1,
          screenshotUrl:"/img/data/item0_2.jpg",
          detail:"이렇게 요리하세요"
        },
        {
          order:2,
          screenshotUrl:"/img/data/item0_3.jpg",
          detail:"저렇게 요리하세요"
        },

        {
          order:3,
          screenshotUrl:"/img/data/item0_4.jpg",
          detail:"그렇게 요리하세요"
        },

        {
          order:4,
          screenshotUrl:"/img/data/item0_5.jpg",
          detail:"열심히 요리하세요"
        },

        {
          order:5,
          screenshotUrl:"/img/data/item0_6.jpg",
          detail:"다음과 같이 요리하세요"
        },
      ]
    },



    {
      recipeId:1,
      mainImageUrl:"/img/data/item1_0.jpg",
      title:"오늘은 짜장면을",
      tags:["짜장면", "중식", "다이어트 파괴자","존맛탱"],
      createdDate:"2023-03-16",
      author:"갱훈",
      commentary:"이 돈가스를 먹고 나서 제 인생이 달라졌습니다 여러분도 만들어서 드셔보십쇼.",
      steps:[
        {
          order:0,
          screenshotUrl:"/img/data/item1_1.jpg",
          detail:"이런 재료들을 준비해주세요"
        },
        {
          order:1,
          screenshotUrl:"/img/data/item1_2.jpg",
          detail:"이렇게 요리하세요"
        },
        {
          order:2,
          screenshotUrl:"/img/data/item1_3.jpg",
          detail:"저렇게 요리하세요"
        },

        {
          order:3,
          screenshotUrl:"/img/data/item1_4.jpg",
          detail:"그렇게 요리하세요"
        },

        {
          order:4,
          screenshotUrl:"/img/data/item1_5.jpg",
          detail:"열심히 요리하세요"
        },

        {
          order:5,
          screenshotUrl:"/img/data/item1_6.jpg",
          detail:"다음과 같이 요리하세요"
        },
      ]
    }
  ];

  const recipeData = items.filter(item => String(item.recipeId) === "0");

  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center">
        <RecipeDetailCard recipe={recipeData[0]} />
      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default RecipeDetailPage;
