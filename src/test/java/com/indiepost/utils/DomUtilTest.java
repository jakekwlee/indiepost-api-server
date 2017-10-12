package com.indiepost.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jake on 10/12/17.
 */
public class DomUtilTest {
    @Test
    public void getImagePrefixesShouldExtractImagePrefixesFromPostContent() throws JsonProcessingException {
        String content = "<p>2017년 10월 9일 오늘은 꼭 &lsquo;체 게바라&rsquo; 50주기 되는 날이다. 이념을 넘어 인류의 진정한 자유와 행복을 위해 싸운 혁명가이자 시대의 우상, 체 게바라를 영화로 만나보자. 그의 궤적을 고스란히 확인할 수 있다. 체 게바라를 위한, 어쩌면 오늘날 우리를 위한 영화들. 순서대로 보면 더 좋다.</p>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/2wHPg1-637x817.jpeg\" alt=\"C:\\Users\\유미래사원님\\AppData\\Local\\Microsoft\\Windows\\INetCache\\Content.Word\\CheHigh.jpg\" />\n" +
                "<figcaption>'영웅적 게릴라'로 불리는 체 게바라의 사진. 1960년 알베르토 코르다가 촬영한 것이다</figcaption>\n" +
                "</figure>\n" +
                "<p>&nbsp;</p>\n" +
                "<h2><strong>1. 여행, 혁명을 향한 시작</strong></h2>\n" +
                "<h2><strong>&lt;모터사이클 다이어리&gt;(2004)</strong></h2>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/iEqD1t-659x443.jpeg\" alt=\"C:\\Users\\유미래사원님\\AppData\\Local\\Microsoft\\Windows\\INetCache\\Content.Word\\movie_imageXY1LQ7EI.JPG\" />\n" +
                "<figcaption>&lt;모터사이클 다이어리&gt; 스틸컷</figcaption>\n" +
                "</figure>\n" +
                "<p>체 게바라의 본명은 에르네스토 라파엘 게바라 데 라 세르나(Ernesto Rafael Guevara de la Serna), 간략히 에르네스토 게바라라고 부르며, 애칭은 &lsquo;푸세&rsquo;다. 훗날 본격적인 혁명가의 길에 뛰어든 그는 스스로 &lsquo;체 게바라&rsquo;라는 이름을 붙였다. 아르헨티나에서 태어난 체 게바라는 자유로운 성향의 부모님 아래에서 독서를 즐기며 자랐다. 아버지를 따라 자연스레 의사를 꿈꿨고, 부에노스아이레스 의대를 졸업했다. 그런 그가 의사 가운 대신 검은 베레모와 군복을 입은 배경은 바로 영화 &lt;모터사이클 다이어리&gt;에 잘 나와 있다.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/u6jz_b80V5g\"></iframe>\n" +
                "<figcaption>&lt;모터사이클 다이어리&gt; 예고편</figcaption>\n" +
                "</figure>\n" +
                "<p>영화는 잘 알려진 사실 그대로 체 게바라가 민중 혁명가로서 신념을 갖게 된 결정적 사건, 친구 알베르토 그라나도와 함께한 남미 여행에 초점을 맞췄다. 낡은 모터사이클에 몸을 싣고 당차게 여행을 시작한 두 친구의 이야기는 얼핏 보면 패기 넘치는 20대 청춘의 로드 무비처럼 보인다. 그러나 무엇보다도 20대 초반의 평범한 대학생이던 게바라가 여행을 통해 어떻게 변화해가는지를 섬세하게 보여주는 이야기이자, 한 청년이 불합리한 사회문제들을 접하면서 겪은 마음 속 변화를 고스란히 느낄 수 있는 영화다. 나아가 이야기 너머로 전해지는 체 게바라의 순수한 열정과 진리에 대한 갈망은 오늘날 많은 사람에게도 삶에 대한 용기를 선사한다.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<h2><strong>2. 혁명가로서의 대장정</strong></h2>\n" +
                "<h2><strong>&lt;체 1부 - 아르헨티나&gt;, &lt;체 2부 - 게릴라&gt;(2008) </strong></h2>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/CtSSuv-700x475.jpeg\" alt=\"C:\\Users\\유미래사원님\\AppData\\Local\\Microsoft\\Windows\\INetCache\\Content.Word\\222.jpg\" />\n" +
                "<figcaption>1959년 혁명군 시절의 체 게바라(좌), 영화 &lt;체 1부&gt; 포스터(우)</figcaption>\n" +
                "</figure>\n" +
                "<p>체 게바라가 남긴 저서들이 증명하듯, 그는 죽기 직전까지도 일기를 썼다. 체 게바라 사후에 이러한 전기 영화가 나올 수 있었던 것은 수많은 생각과 행적들을 글로 남기며 끝까지 신념을 지킨 체 게베라의 공일 것이다. 앞서 소개한 영화 &lt;모터사이클 다이어리&gt; 역시 체 게바라가 여행하며 직접 쓴 기록을 엮은 책을 원작으로 한다. 체 게바라를 혁명의 길로 인도한 여행기를 감상했다면, 이제 진정한 체 게바라 혁명의 대장정을 볼 차례다. 스티븐 소더버그 감독이 연출하고 명배우 베나치오 델 토로가 체 게바라를 연기한 영화 &lt;체&gt;를 감상해보자.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/O7JIkJDxbzU\"></iframe>\n" +
                "<figcaption>&lt;체&gt; 예고편</figcaption>\n" +
                "</figure>\n" +
                "<p>장장 4시간 러닝타임을 보유한 영화 &lt;체&gt;는 1부와 2부로 나뉘어 혁명군 체 게바라의 삶을 그대로 재현한다. 여행에서 피폐한 남미의 현실을 보고 혁명을 결심한 체 게바라는 아르헨티나를 떠나 과테말라로 떠난다. 그곳에서 혁명에 가장 큰 영향을 준 쿠바의 정치인 피델 카스트로를 만나, 독재 정권이 집권하고 있는 쿠바를 향해 혁명을 마음먹는다. 영화 &lt;체 1부&gt;는 쿠바 혁명에 참여하여 성공을 거두기까지의 과정을 그린다. &lt;체 2부&gt;에서는 이후 쿠바를 떠난 체 게바라가 더 넓은 남미지역으로의 혁명을 위해 볼리비아에서 펼친 게릴라 투쟁, 그곳에서 최후를 맞기까지의 여정을 다룬다. 감독은 역사적 사실 그대로 체 게바라의 발자취를 담고자 했고, 베나치오 델 토로는 외양마저 훌륭하게 연기했다. 가히 체 게바라의 일생을 한눈에 보여주는 다큐멘터리라 해도 손색없는 작품이다.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<h2><strong>3. 시대의 아이콘이 되다</strong></h2>\n" +
                "<h2><strong>&lt;체 게바라 : 뉴맨&gt;(2010)</strong></h2>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/gJpkDf-700x497.jpeg\" alt=\"C:\\Users\\유미래사원님\\AppData\\Local\\Microsoft\\Windows\\INetCache\\Content.Word\\333.jpg\" />\n" +
                "<figcaption>&lt;체 게바라 : 뉴맨&gt; 해외, 국내 포스터</figcaption>\n" +
                "</figure>\n" +
                "<p>마치 실제 같은 배우들의 열연을 통해 체 게바라의 일생을 훑어봤다면, 이제는 혁명가 체 게바라의 내면세계를 한층 깊이 들여다볼 차례다. 아르헨티나 감독 트리스탄 바우에르가 연출한 다큐멘터리 영화 &lt;체 게바라 : 뉴맨&gt;은 체 게바라가 마지막으로 체포된 볼리비아의 군사기록보관소에서 발견한 새로운 자료들과 쿠바에 생존하는 가족들의 증언, 세상에 처음 공개되는 체 게바라의 육성 자료 등을 통해 오늘날 체 게바라를 다시 말한다. 특히 그때까지 공개되지 않은 체 게바라의 기록물을 내레이션으로 읊어주는 방식으로 영화 곳곳에서 체 게바라의 어조마저 느낄 수 있다. 반세기가 되도록 수없이 회자하고 재현되어 누구나 아는 이름이 체 게바라이지만, 정작 그의 삶을 제대로 아는 사람은 그리 많지 않을 것이다. 오늘, 체 게바라를 깊이 생각해보기 좋은 날이다.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/NPsMPo8F7Ok\"></iframe>\n" +
                "<figcaption>&lt;체 게바라 : 뉴맨&gt; 예고편</figcaption>\n" +
                "</figure>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>[[rel=\"3905,80,3828,320\"]]</p>\n" +
                "<p>&nbsp;</p>";
        List<String> imageSetPrefixList = DomUtil.getImagePrefixes(content);
        int expectedNumberOfImages = 4;
        List<String> expectedImageSetPrefixList = Arrays.asList(
                "2017/10/2wHPg1", "2017/10/iEqD1t", "2017/10/CtSSuv", "2017/10/gJpkDf"
        );

        Assert.assertEquals(expectedNumberOfImages, imageSetPrefixList.size());
        Assert.assertTrue(CollectionUtils.isEqualCollection(expectedImageSetPrefixList, imageSetPrefixList));
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(imageSetPrefixList);
        System.out.println(result);
    }
}
