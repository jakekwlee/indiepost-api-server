package com.indiepost.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.dto.post.RelatedPostsMatchingResult;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jake on 10/12/17.
 */
public class DomUtilTest {
    @Test
    public void getImagePrefixes_ShouldExtractImagePrefixesFromPostContent() throws JsonProcessingException {
        String content = "<p>인디포스트는 동영상 스트리밍 서비스 옥수수(oksusu)와 함께 작품성 있는 영화를 선별해 추천하는 무료영화 이벤트를 진행합니다. 10월 14일 토요일부터 16일 월요일까지, 3일 동안 통신사 관계없이 누구나 옥수수에서 무료로 영화를 감상할 수 있습니다. 모바일 앱과 웹사이트에서 모두 볼 수 있으며, 중단 후에는 언제든지 &lsquo;이어보기&rsquo;로 다시 감상할 수 있습니다.</p>\n" +
                "<p>이번에 추천할 영화는 바로 &lt;데이빗 린치: 아트 라이프&gt;입니다.</p>\n" +
                "<figure class=\"image ad-video-player\"><a href=\"http://www.indiepost.co.kr/link/b374da5b\" target=\"_target\" rel=\"noopener\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/13/hJVwPo-665x425.png\" width=\"665px\" height=\"425px\" /><span class=\"play-btn\">&nbsp;</span> <span class=\"video-title\">데이빗 린치: 아트 라이프(2017)</span> <span class=\"video-logo\">&nbsp;</span> </a>\n" +
                "<figcaption>(클릭 시 무료 재생 페이지로 이동합니다)</figcaption>\n" +
                "</figure>\n" +
                "<p>&lsquo;컬트무비&rsquo;라는 말 들어본 적 있나요? 어딘가 난해하고 음습하지만 자기만의 독창적인 예술세계를 녹여내어 대중성은 적지만 마니아들을 열광하게 만드는 영화, 컬트무비라는 용어를 탄생시킨 장본인이 바로 데이빗 린치(David Lynch)입니다. 그렇기에 컬트영화의 팬이 아니면 데이빗 린치는 잘 모를 수도 있는 감독이지만, 그의 영화들은 한 번쯤 들어보았을 겁니다. 대표적으로 지난해 영국 공영방송 BBC가 선정한 &lsquo;21세기 최고의 영화&rsquo; 리스트에서 1위를 차지하며 화제가 된 작품 &lt;멀홀랜드 드라이브&gt;(2001)가 바로 데이빗 린치의 영화입니다. 현실과 환상이 충돌하는 곳이라 설명하는 영화답게, 데이빗 린치 특유의 난해하면서도 아름다운 분위기가 집약된 영화입니다. 또 감독의 대표작으로 꼽히는 &lt;인랜드 엠파이어&gt;(2006), &lt;로스트 하이웨이&gt;(1997), &lt;트윈 픽스&gt;(1992), &lt;블루 벨벳&gt;(1986) 같은 영화 역시 어렵고 난해하지만, 팬들에게 폭발적인 지지를 받고 있죠.</p>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/13/k5q1j8-665x323.jpeg\" width=\"665px\" height=\"323px\" />\n" +
                "<figcaption>&lt;데이빗 린치: 아트 라이프&gt; 스틸컷</figcaption>\n" +
                "</figure>\n" +
                "<p>지난 9월 21일 국내 개봉한 영화 &lt;데이빗 린치: 아트 라이프&gt;는 영화감독을 넘어서 독보적인 예술가로 추앙받는 데이빗 린치의 예술세계를 깊이 들여다보는 다큐멘터리입니다. 데이빗 린치의 작품을 좋아하는 사람들에게는 더할 나위 없이 매력적인 영화이며, 데이빗 린치의 세계를 한 번도 접해본 적 없는 사람에게는 어쩌면 그 &lsquo;컬트&rsquo;한 세계로 입문할 수 있는 친절한 통로가 될 수도 있습니다.</p>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/13/BaQ5b8-700x332.jpeg\" width=\"700px\" height=\"332px\" />\n" +
                "<figcaption>&lt;멀홀랜드 드라이브&gt;, &lt;로스트 하이웨이&gt;, &lt;블루 벨벳&gt; 포스터</figcaption>\n" +
                "</figure>\n" +
                "<p>최근 CGV아트하우스에서는 &lt;데이빗 린치: 아트 라이프&gt; 개봉을 기념하여 데이빗 린치의 대표작들을 함께 상영하는 특별전을 열기도 했습니다. 9월부터 진행한 특별전은 오는 10월 18일까지 서울과 광주 등지에서 마무리를 짓는다고 합니다. &lt;데이빗 린치: 아트 라이프&gt; 역시 아직 극장에서 상영 중이지만, 대부분 막을 내리고 있습니다. 그런 점에서 이번 주말 동안 데이빗 린치의 영화 한 편을 무료로 감상하는 것은 더욱 반가운 기회일 것 같네요.</p>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/13/lxJMjF-665x374.jpeg\" width=\"665px\" height=\"374px\" />\n" +
                "<figcaption>&lt;데이빗 린치: 아트 라이프&gt; 스틸컷</figcaption>\n" +
                "</figure>\n" +
                "<p>영화 &lt;데이빗 린치: 아트 라이프&gt;는 데이빗 린치가 자신의 어린 딸 룰라 린치에게 말하는 듯 나직한 내레이션으로 시작합니다. 데이빗 린치가 자신의 기억을 회상하고 이야기하는 방식으로, 무엇보다 미술을 전공한 데이빗 린치 감독이 어떤 그림을 그리고, 어느 것으로부터 어떤 식으로 영감을 얻는지 직접 들려줍니다. 천재적인 상상력의 소유자가 말하는 자신의 이야기는 그 자체로 흥미롭습니다.</p>\n" +
                "<p>또 다큐멘터리 영화이지만 역시 예술적, 감각적인 연출을 선보이는 덕에 데이빗 린치의 극영화를 보여주는 듯한 느낌을 줍니다. 공동 연출자인 존 구옌 감독부터 이미 데이빗 린치의 열성 팬이라고 하니, 자연스레 &lsquo;컬트적&rsquo;인 다큐멘터리를 지향한 셈입니다. 데이빗 린치의 과거와 현재, 그의 외면부터 무의식까지 두루 들여다보는 영화를 보고 나면 제목 그대로 &lsquo;아트 라이프&rsquo;란 무엇인지 생생하게 체험할 수 있습니다.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<h3>지금, &lt;데이빗 린치: 아트 라이프&gt; 감상하기</h3>\n" +
                "<p>&lt;데이빗 린치: 아트 라이프&gt;는 10월 14일 오전 10시부터 10월 17일 오전 9시까지 옥수수 모바일 앱과 홈페이지에서 무료로 볼 수 있다. 아래 링크에서 바로 재생 가능하며, 옥수수 앱 미설치자는 설치 후 바로 감상할 수 있다.</p>\n" +
                "<figure class=\"image ad-video-player\"><a href=\"http://www.indiepost.co.kr/link/b374da5b\" target=\"_target\" rel=\"noopener\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2017/10/13/hJVwPo-665x425.png\" width=\"665px\" height=\"425px\" /><span class=\"play-btn\">&nbsp;</span> <span class=\"video-title\">데이빗 린치: 아트 라이프(2017)</span> <span class=\"video-logo\">&nbsp;</span> </a>\n<p>&nbsp;</p>\n" +
                "<figcaption>(클릭 시 무료 재생 페이지로 이동합니다)</figcaption>\n" +
                "</figure>\n" +
                "<h5><strong>[옥수수에서 무료로 영화 감상하기]</strong></h5>\n" +
                "<h5>모바일 동영상 서비스 '옥수수(oksusu)'에서는 매주 토요일마다 최신 영화 한 편을 무료로 제공한다. 앱을 다운로드 받으면 이동통신사 상관없이 모두 감상할 수 있다. 또한 매주 수요일에는 영화제 수상작 또는 작품성 있는 예술영화 한 편을 선정하여 무료로 제공한다. 그 밖에 옥수수 앱 내 &lsquo;전국민 무료 VOD&rsquo; 메뉴에서는 역시 이동통신사 상관없이 다양한 영화를 언제든지 무료로 감상할 수 있다.</h5>" +
                "<p>[[rel=\"6014,6368,6768,6672\"]]</p>";
        Set<String> imageSetPrefixList = DomUtil.getImagePrefixes(content);
        int expectedNumberOfImages = 4;
        List<String> expectedImageSetPrefixList = Arrays.asList(
                "2017/10/13/hJVwPo", "2017/10/13/k5q1j8", "2017/10/13/BaQ5b8", "2017/10/13/lxJMjF"
        );

        assertThat(imageSetPrefixList.size()).isEqualTo(expectedNumberOfImages);
        assertThat(CollectionUtils.isEqualCollection(expectedImageSetPrefixList, imageSetPrefixList)).isTrue();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(imageSetPrefixList);
        System.out.println(result);
    }

    @Test
    public void getRelatedPostIdsFromPostContent_shouldReturnRelatedPostsResultProperly() {
        String content = "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2018/03/05/VHpkQ9-560x315.jpeg\" alt=\"Trapped_image_TV.jpg\" /></figure>\n" +
                "<p>미디어 기술의 발전에 따라 전 세계의 드라마를 안방에서 편하게 볼 수 있는 세상이 되었다. 그렇다 쳐도 대서양 북단, 아이슬란드어라는 고유 언어를 사용하는 인구 34만 명의 섬나라 아이슬란드(Iceland)의 드라마를 볼 수 있는 통로는 그리 흔치 않다. 하지만 아이슬란드에서 제작된 10부작 미스터리 수사 드라마 &lt;트랩트(Trapped)&gt;는 2015년 토론토 국제영화제에 첫선을 보이며 금방 화제작으로 떠올랐다. BBC를 포함한 주요 방송사를 통해 유럽 지역에 방송되었고, 넷플릭스가 판권을 인수하며 우리나라에도 스트리밍 서비스를 시작하였다.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/uUP5uNDV6DA\"></iframe>\n" +
                "<figcaption>아이슬란드 드라마 &lt;트랩트&gt; 예고편</figcaption>\n" +
                "</figure>\n" +
                "<p>아이슬란드 하면 트립합 뮤지션 비요크(Bjork)나, 바로 얼마 전 사망한 영화음악 감독 조한 조한슨(Johann Johannsson), 그리고 유명한 오로라 관광지라는 점들을 떠올릴 수 있을 것이다. 바이킹의 후손과 스칸디나비아반도에서 건너온 이주자들이 오랜 식민지 생활을 극복하고 1944년 덴마크로부터 독립을 쟁취하면서 설립한 국가다.</p>\n" +
                "<figure class=\"image\"><img src=\"https://cdn.indiepost.co.kr/uploads/images/2018/03/05/JrTFyg-700x466.jpeg\" />\n" +
                "<figcaption>아이슬란드의 발타사르 코르마우퀴르 감독</figcaption>\n" +
                "</figure>\n" +
                "<p>재난 영화 &lt;에베레스트&gt;(2015)로 국내에서 관객수 33만 명을 기록한 아이슬란드의 국민 감독 발타사르 코르마우퀴르(Baltasar Kormakur)가 제작에 나섰고, 얼마 전 사망 소식이 전해진 세계적인 영화음악가 조한 조한슨이 영화음악에 참여했다. EU 산하 문화펀드인 크리에이티브 유럽(Creative Europe)의 지원을 받으며 제작비 6백5십만 유로(약 90억 원)의 대형 드라마 제작을 추진하였다. 아이슬란드 평균 드라마 제작비의 5배에 이른다고 하니 얼마나 과감한 투자였는지 짐작할 수 있다.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/yueAc8Dqazg\"></iframe>\n" +
                "<figcaption>&lt;트랩트&gt;의 인트로. 대자연의 빙하와 인간의 피부를 대비한 비주얼이 독특하다</figcaption>\n" +
                "</figure>\n" +
                "<p>10부작 &lt;트랩트&gt;는 감독이 &ldquo;노르딕 느와르와 아가사 크리스티의 결합&rdquo;이라고 정의했듯이, 크리스티 스타일의 미스터리 전개 방식을 따른다. 대부분의 주민들이 서로 알고 지내는 작은 바닷가 마을, 이곳의 경찰서에는 세 명의 경찰만이 근무하며 총기를 휴대하지 않을 정도로 한적하다. 네덜란드의 선박이 입항할 무렵, 인근 바다에서 토막 사체가 발견되면서 드라마는 시작한다. 동시에 눈 폭풍이 몰아치며 마을은 고립되고, 가족 문제로 힘들어하는 서장 &lsquo;안드레(Andri)&rsquo;는 한꺼번에 닥친 난관을 헤치고 사건을 풀어 나간다.</p>\n" +
                "<figure class=\"video image\"><iframe width=\"560\" height=\"314\" src=\"//www.youtube.com/embed/FsSNjrscwpI\"></iframe>\n" +
                "<figcaption>BBC의 &lt;트랩트&gt; 예고 영상</figcaption>\n" +
                "</figure>\n" +
                "<p>이 드라마에서 멋진 액션이나 날카로운 추리극의 묘미를 기대하기는 어렵다. 하지만 영국 BBC의 방송 후, 한 평론가는 &ldquo;드라마에 너무 몰입한 나머지 자막 방송이라는 걸 깜박 잊고 볼륨을 올렸다&rdquo;라 평했듯 조용하지만 치밀하게 사건의 실체에 근접한다. 이제 안드레 서장이 등장하는 &lt;트랩트 2&gt;가 2018년 겨울 방송을 목표로 제작에 들어갔다. 다행히 첫 시즌 내내 시청자를 갑갑하게 만든 눈 폭탄은 나오지 않을 것이라는 소식이다.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>[[rel=\"5731,5526,4996,3887\"]]</p>";
        RelatedPostsMatchingResult result = DomUtil.getRelatedPostIdsFromPostContent(content);
        assertThat(result.getIds()).containsExactlyElementsOf(Arrays.asList(5731L, 5526L, 4996L, 3887L));
        assertThat(result.getContent()).doesNotContain("<p>[[rel=\"5731,5526,4996,3887\"]]</p>");
    }
}
