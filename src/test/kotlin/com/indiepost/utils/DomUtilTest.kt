package com.indiepost.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.indiepost.dto.LinkMetadataResponse
import org.apache.commons.collections4.CollectionUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

/**
 * Created by jake on 10/12/17.
 */
class DomUtilTest {
    @Test
    @Throws(JsonProcessingException::class)
    fun extractMetadataFromUrl_workProperly() {
        val url = "https://www.theguardian.com/international"
        val res: LinkMetadataResponse? = DomUtil.extractMetadataFromUrl(url)
        assertThat(res).isNotNull()
        res?.let {
            assertThat(it.id).isNotNull()
            assertThat(it.title).isEqualTo("News, sport and opinion from the Guardian's global edition | The Guardian")
            assertThat(it.imageUrl).isEqualTo("https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png")
            assertThat(it.url).isEqualTo("http://www.theguardian.com/international")
            assertThat(it.description).isEqualTo("Latest international news, sport and comment from the Guardian")
            assertThat(it.source).isEqualTo("www.theguardian.com")
        }
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun extractMetadataFromUrl_withNaverMovieURL_workProperly() {
        val url = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=171725"
        val res: LinkMetadataResponse? = DomUtil.extractFlimMetadataFromUrl(url)
        assertThat(res).isNotNull()
        res?.let {
            assertThat(it.id).isNotNull()
            assertThat(it.productId).isNotNull()
            assertThat(it.title).isEqualTo("스파이더맨: 뉴 유니버스")
            assertThat(it.imageUrl).isEqualTo("https://movie-phinf.pstatic.net/20181207_3/1544172927548CbuaX_JPEG/movie_image.jpg?type=m665_443_2")
            assertThat(it.source).isEqualTo("movie.naver.com")
            assertThat(it.url).isEqualTo(url)
            assertThat(it.directors).isEqualTo(Arrays.asList("밥 퍼시케티", "피터 램지", "로드니 로스맨"))
            assertThat(it.actors).isEqualTo(Arrays.asList("샤메익 무어", "헤일리 스테인펠드", "니콜라스 케이지"))
            assertThat(it.published).isEqualTo("2018")
        }
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun extractBookMetadataFromUrl_withNaverBookURL_workProperly() {
        val url = "https://book.naver.com/bookdb/book_detail.nhn?bid=9587007"
        val res: LinkMetadataResponse? = DomUtil.extractBookMetadataFromUrl(url)
        assertThat(res).isNotNull()
        res?.let {
            assertThat(it.id).isNotNull()
            assertThat(it.productId).isNotNull()
            assertThat(it.title).isEqualTo("희지의 세계")
            assertThat(it.imageUrl).isEqualTo("https://bookthumb-phinf.pstatic.net/cover/095/870/09587007.jpg?type=m5")
            assertThat(it.source).isEqualTo("book.naver.com")
            assertThat(it.url).isEqualTo(url)
            assertThat(it.authors).isEqualTo(Arrays.asList("황인찬"))
            assertThat(it.publisher).isEqualTo("민음사")
            assertThat(it.published).isEqualTo("2015.09.18")
        }
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getImagePrefixes_ShouldExtractImagePrefixesFromPostContent() {
        val content = "<p>인디포스트는 동영상 스트리밍 서비스 옥수수(oksusu)와 함께 작품성 있는 영화를 선별해 추천하는 무료영화 이벤트를 진행합니다. 10월 14일 토요일부터 16일 월요일까지, 3일 동안 통신사 관계없이 누구나 옥수수에서 무료로 영화를 감상할 수 있습니다. 모바일 앱과 웹사이트에서 모두 볼 수 있으며, 중단 후에는 언제든지 &lsquo;이어보기&rsquo;로 다시 감상할 수 있습니다.</p>\n" +
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
                "<p>[[rel=\"6014,6368,6768,6672\"]]</p>"
        val imageSetPrefixList = DomUtil.getImagePrefixes(content)
        val expectedNumberOfImages = 4
        val expectedImageSetPrefixList = Arrays.asList(
                "2017/10/13/hJVwPo", "2017/10/13/k5q1j8", "2017/10/13/BaQ5b8", "2017/10/13/lxJMjF"
        )

        assertThat(imageSetPrefixList.size).isEqualTo(expectedNumberOfImages)
        assertThat(CollectionUtils.isEqualCollection(expectedImageSetPrefixList, imageSetPrefixList)).isTrue()
        val objectMapper = ObjectMapper()
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(imageSetPrefixList)
        println(result)
    }

    @Test
    fun findWriterInformationFromContent() {
        val content = """
            <p>페미니즘 작가, 걸리 포토. 어떤 수식어도 필요 없는 너무 잘난 여자 사진가 셋을 말한다.<br /><br /></p>
<h2>모니카 모기(Monika Mogi)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/veZvAG-666x995.jpeg" width="666px" height="995px" />
<figcaption>▲ &lt;루키&gt; 매거진을 위해 엘레노어 하드윅(Eleanor Hardwick)이 촬영한 모니카 모기</figcaption>
</figure>
<p>일명 모니모기. 철 지난 일본 아이돌그룹 이름 같은 애칭을 가진 모니카 모기는 이제 막 20대 중반이 된 사진가다. 사진가로서 경력은 이미 10대부터 시작했다. 인터넷에 능통하고 외로운 열다섯 살 모니카 모기는 영화 &lt;올모스트 페이머스&gt;(2000)를 보고 주인공처럼 되길 원했다. 영화처럼 잡지사에 연락해 음악가 사진을 찍었고 그 사진을 블로그에 올렸다. 사진을 본 동갑내기 캐나다 사진가 페트라 콜린스(Petra Collins)의 제안으로 여성주의 사진 그룹에서 함께 활동하면서 이름을 알렸다. 첫 상업사진은 아메리칸 어패럴 광고였는데, 주위 친구들과 자신이 피사체가 되었다. 지금은 이런 비슷한 흐름으로 사진가의 수순을 밟는 이들이 늘었지만, 그가 10대의 나이로 발탁되었을 때는 큰 반향이 일었다. 히로믹스가 비슷한 나이에 주위를 천진하게 찍었다면 모니카 모기는 의젓하게 찍었다고 말할 수 있다. 거침없고 꾸밈없는 비 전문모델이자 친구들을 필름카메라로 적나라하게 담았다. 흐릿하게 마구잡이로 찍는 기교는 부리지 않았다. 피사체를 선명하고 강렬하게 담았다. 여전히 그의 사진 속 여자들은 전형적인 아름다움과는 거리가 멀다. 그도 어릴 때는 빅토리아 시크릿 모델을 아름답게 느꼈다고 한다. 아메리칸 어패럴 광고 속에서 접힌 배를 드러내고 가슴 크기가 어떻든 상관하지 않는 자유로운 몸을 보고 직접 찍고 난 후로는 매체에서 지정하지 않는 이상 주변에서 피사체를 찾는다. 그는 특별히 사진을 찍을 때 페미니즘적인 메시지를 담으려고 하지 않는다고 하지만 보는 이들은 모니카 모기의 사진에서 익숙하지 않은 여성의 모습을 발견하고 새긴다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/nhoSUl-700x232.jpeg" width="700px" height="232px" />
<figcaption>▲ 아메리칸 어패럴 광고와 옥외 광고를 찍은 사진<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/TwVufA-700x322.jpeg" width="700px" height="322px" />
<figcaption>▲ 파르코 백화점의 CM에서 많은 영감을 얻는다는 그의 말처럼 어떤 사진은 쇼와 시대의 기운이 감돈다<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/pc4sIR-677x907.jpeg" width="677px" height="907px" />
<figcaption>▲ 무라카미 류의 소설 &lt;LOVE &amp; POP&gt;을 모티프로 작업한 사진 시리즈<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/aoTvKV-700x516.jpeg" width="700px" height="516px" />
<figcaption>▲ 1992년생인 그는 태어나자마자 사라져버린 90년대 무드를 찍는 것에 신기할 정도로 능통하다. 모델은 미즈하라 키코</figcaption>
</figure>
<p>&nbsp;</p>
<h2>한나 문(Hanna Moon)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/qbg5mC-480x600.jpeg" width="480px" height="600px" />
<figcaption>▲ 2호 표지에서 오노 요코 역할을 한 건 작가 자신이다</figcaption>
</figure>
<p>한나 문은 &lt;A NICE MAGAZINE&gt;의 사진가이자 발행인이다. 대학 졸업작품으로 구상한 것을 실제로 출판시장에 내놓았다. 이제 2호까지 나온 &lt;A NICE MAGAZINE&gt;은 동시대의 화려한 유행과는 거리가 멀다. 패션지지만 지금 당장 빼놓을 수 없는 브랜드나 모델이 요란하게 등장하지 않는다. 한나 문과 그의 친구들, 몇 디자이너의 의상이 두서없이 섞여 있다. 패션과 사진, 일상과 비일상이 뒤섞인 모양새다. 확대한 얼굴 사진 위에 텍스트 박스를 마구잡이로 흩어놓은 것과 같이 말이다. 1호에서 한나 문의 화보는 서너 개쯤 된다. 비슷한 얼굴에 비슷한 머리 모양을 한 이화여자대학 졸업생의 사진을 채집한 페이지, 친한 친구들에게 사카이, 꼼 데 가르송 같은 브랜드를 입히고 찍은 패션 화보에 동시에 그의 이름이 올라있다. 비닐봉지를 손에 걸고 커다란 채소를 입에 문 조이스라는 이름의 여성은 누구의 친구인지 모델인지, 뭘 하는 건지 제대로 알 수 없다. 다만 크레딧에 협찬 의상 문구를 보고 그제야 옷을 보여주는 패션화보라고 인식하게 된다. 짧은 머리 여성을 잔뜩 모아놓은 화보도 있다. 손으로 쓸어주고 싶은 귀여운 잔머리, 긴 속눈썹과 대비되는 짧게 깎은 옆머리가 페이지 안을 가득 채우고 있다. 일련의 작업물을 보면 한나 문이 여성, 사진 속 피사체를 대하는 태도는 더없이 담백하다. 강하고 딱딱하지도 부드럽고 연약하지도 않다. 얼마 전에 나온 2호에는 오노 요코와 존 레논의 전설적인 사진을 오마주해 표지로 실었다. 어쩜 아무런 긴장감이 안 느껴진다. 정지 화면의 캡처 같다. 이건 칭찬이다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/EaOtL2-700x484.jpeg" width="700px" height="484px" />
<figcaption>▲ &lt;A NICE MAGAZINE&gt; 1호. 나오자마자 &lt;i-D&gt;, &lt;DAZED &amp; CONFUSED&gt; 같은 패션 매거진에서 바쁘게 다뤘다<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/lzyecv-640x879.jpeg" width="640px" height="879px" />
<figcaption>▲ 한나 문의 친구 조이스. 패션과 라이프 스타일, 포토그라피가 한 데 섞인, &lt;A NICE MAGAZINE&gt;의 슬로건과 들어맞는 사진 한 장<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/oF6w7f-700x314.jpeg" width="700px" height="314px" />
<figcaption>▲ 그 밖의 사진들<br /><br /></figcaption>
</figure>
<p>&nbsp;</p>
<h2>샌디 킴(Sandy Kim)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/MGx09L-700x462.jpeg" width="700px" height="462px" />
<figcaption>▲ 샌디 킴의 사진은 일상과 가깝기 때문에 블로그에 그날의 기록처럼 올라오곤 한다. 요 몇 년간은 업데이트가 되지 않았다. 블로그에서 가져온 셀프 사진</figcaption>
</figure>
<p>여성을 모델로 찍는 남자 사진가는 많다. 사진 속에서 은근히 성적인 뉘앙스를 풍기기도 한다. 잠자리를 하고 난 직후를 떠올리게 하거나 셔터를 몇 번 누르고 나면 그런 일이 벌어질 것이라는 암시를 주는 사진들 말이다. 그런 부분에서 여성 사진가들은 억울하다. 사진가 샌디 킴은 10년 전부터 앞뒤 안 가리고 사진을 찍었다. 남자친구의 벗은 몸과 자신의 벗은 몸을 아무렇지 않게. 사랑을 나누고 나서의 결과물까지도 여과 없이 사진으로 담았다. 예쁘게 보이려 결점을 가리고, 심의에 걸릴 만한 것을 거르는 일 따위는 없다. 샌디 킴은 막 20대에 들어섰을 때 사진기를 들고 주변에 일어나는 모든 일을 찍어댔다. 약과 음악, 섹스와 흥분, 쾌락. 빠르게 지나 사라지는 것들을 기억하기 위해 사진기를 들었다고 한다. 샌디 킴이 담은 홀딱 벗고 마리화나를 태우는 친구의 모습, 술을 마시고 난 후의 잔재, 금괴처럼 쌓아 올린 빈 담뱃갑은 외면하고 싶은 장면임과 동시에 너무 순진할 정도로 사실적이다. 이렇게 솔직한 사진을 찍는 사진가는 아직까지 샌디 킴 이외에 보지 못했다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/iyWnei-700x236.jpeg" width="700px" height="236px" />
<figcaption>▲ 홈페이지에는 성적이고 노골적인 사진만 모은 트리플 엑스 섹션이 있다. 그런데 뭐가 이상한가. 보다 보면 다 너무 당연한 사진들이다. 연인이 샌디 킴에게 보낸 사랑의 문자<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/wsfg23-700x947.jpeg" width="700px" height="947px" />
<figcaption>▲ 고단한 뉴욕의 삶<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/2BAXCX-700x236.jpeg" width="700px" height="236px" />
<figcaption>▲ 밴드 걸스의 사진을 꾸준히 찍었다. 샌디 킴 사진의 반은 너무나 아름다운 빛을 담고 있다</figcaption>
</figure>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/uchcNJ-700x1405.jpeg" width="700px" height="1405px" />
<figcaption>▲ 그 밖의 사진들</figcaption>
</figure>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/itWQWa-539x376.jpeg" width="539px" height="376px" /></figure>
<p>ㅣ필자소개ㅣ<br /><strong>박의령(Park ui ryung)</strong><br />매거진 &lt;DAZED &amp; CONFUSED&gt;, &lt;NYLON&gt; 피처 에디터를 거쳐 에어서울 항공 기내지 &lt;YOUR SEOUL&gt;을 만들고 있다. 이상한 만화, 영화, 음악을 좋아하고 가끔 사진을 찍는다. 윗옷을 벗은 여성들을 찍은 음반 겸 사진집 &lt;75A&gt;에 사진가로 참여했다.</p>
<p>박의령 인스타그램 [<a href="https://www.instagram.com/youryung/" target="_blank" rel="noopener noreferrer">바로가기</a>]</p>
<p>&nbsp;</p>
        """.trim().trimIndent()
        val expected = """
                       <p>페미니즘 작가, 걸리 포토. 어떤 수식어도 필요 없는 너무 잘난 여자 사진가 셋을 말한다.<br /><br /></p>
<h2>모니카 모기(Monika Mogi)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/veZvAG-666x995.jpeg" width="666px" height="995px" />
<figcaption>▲ &lt;루키&gt; 매거진을 위해 엘레노어 하드윅(Eleanor Hardwick)이 촬영한 모니카 모기</figcaption>
</figure>
<p>일명 모니모기. 철 지난 일본 아이돌그룹 이름 같은 애칭을 가진 모니카 모기는 이제 막 20대 중반이 된 사진가다. 사진가로서 경력은 이미 10대부터 시작했다. 인터넷에 능통하고 외로운 열다섯 살 모니카 모기는 영화 &lt;올모스트 페이머스&gt;(2000)를 보고 주인공처럼 되길 원했다. 영화처럼 잡지사에 연락해 음악가 사진을 찍었고 그 사진을 블로그에 올렸다. 사진을 본 동갑내기 캐나다 사진가 페트라 콜린스(Petra Collins)의 제안으로 여성주의 사진 그룹에서 함께 활동하면서 이름을 알렸다. 첫 상업사진은 아메리칸 어패럴 광고였는데, 주위 친구들과 자신이 피사체가 되었다. 지금은 이런 비슷한 흐름으로 사진가의 수순을 밟는 이들이 늘었지만, 그가 10대의 나이로 발탁되었을 때는 큰 반향이 일었다. 히로믹스가 비슷한 나이에 주위를 천진하게 찍었다면 모니카 모기는 의젓하게 찍었다고 말할 수 있다. 거침없고 꾸밈없는 비 전문모델이자 친구들을 필름카메라로 적나라하게 담았다. 흐릿하게 마구잡이로 찍는 기교는 부리지 않았다. 피사체를 선명하고 강렬하게 담았다. 여전히 그의 사진 속 여자들은 전형적인 아름다움과는 거리가 멀다. 그도 어릴 때는 빅토리아 시크릿 모델을 아름답게 느꼈다고 한다. 아메리칸 어패럴 광고 속에서 접힌 배를 드러내고 가슴 크기가 어떻든 상관하지 않는 자유로운 몸을 보고 직접 찍고 난 후로는 매체에서 지정하지 않는 이상 주변에서 피사체를 찾는다. 그는 특별히 사진을 찍을 때 페미니즘적인 메시지를 담으려고 하지 않는다고 하지만 보는 이들은 모니카 모기의 사진에서 익숙하지 않은 여성의 모습을 발견하고 새긴다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/nhoSUl-700x232.jpeg" width="700px" height="232px" />
<figcaption>▲ 아메리칸 어패럴 광고와 옥외 광고를 찍은 사진<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/TwVufA-700x322.jpeg" width="700px" height="322px" />
<figcaption>▲ 파르코 백화점의 CM에서 많은 영감을 얻는다는 그의 말처럼 어떤 사진은 쇼와 시대의 기운이 감돈다<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/pc4sIR-677x907.jpeg" width="677px" height="907px" />
<figcaption>▲ 무라카미 류의 소설 &lt;LOVE &amp; POP&gt;을 모티프로 작업한 사진 시리즈<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/aoTvKV-700x516.jpeg" width="700px" height="516px" />
<figcaption>▲ 1992년생인 그는 태어나자마자 사라져버린 90년대 무드를 찍는 것에 신기할 정도로 능통하다. 모델은 미즈하라 키코</figcaption>
</figure>
<p>&nbsp;</p>
<h2>한나 문(Hanna Moon)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/qbg5mC-480x600.jpeg" width="480px" height="600px" />
<figcaption>▲ 2호 표지에서 오노 요코 역할을 한 건 작가 자신이다</figcaption>
</figure>
<p>한나 문은 &lt;A NICE MAGAZINE&gt;의 사진가이자 발행인이다. 대학 졸업작품으로 구상한 것을 실제로 출판시장에 내놓았다. 이제 2호까지 나온 &lt;A NICE MAGAZINE&gt;은 동시대의 화려한 유행과는 거리가 멀다. 패션지지만 지금 당장 빼놓을 수 없는 브랜드나 모델이 요란하게 등장하지 않는다. 한나 문과 그의 친구들, 몇 디자이너의 의상이 두서없이 섞여 있다. 패션과 사진, 일상과 비일상이 뒤섞인 모양새다. 확대한 얼굴 사진 위에 텍스트 박스를 마구잡이로 흩어놓은 것과 같이 말이다. 1호에서 한나 문의 화보는 서너 개쯤 된다. 비슷한 얼굴에 비슷한 머리 모양을 한 이화여자대학 졸업생의 사진을 채집한 페이지, 친한 친구들에게 사카이, 꼼 데 가르송 같은 브랜드를 입히고 찍은 패션 화보에 동시에 그의 이름이 올라있다. 비닐봉지를 손에 걸고 커다란 채소를 입에 문 조이스라는 이름의 여성은 누구의 친구인지 모델인지, 뭘 하는 건지 제대로 알 수 없다. 다만 크레딧에 협찬 의상 문구를 보고 그제야 옷을 보여주는 패션화보라고 인식하게 된다. 짧은 머리 여성을 잔뜩 모아놓은 화보도 있다. 손으로 쓸어주고 싶은 귀여운 잔머리, 긴 속눈썹과 대비되는 짧게 깎은 옆머리가 페이지 안을 가득 채우고 있다. 일련의 작업물을 보면 한나 문이 여성, 사진 속 피사체를 대하는 태도는 더없이 담백하다. 강하고 딱딱하지도 부드럽고 연약하지도 않다. 얼마 전에 나온 2호에는 오노 요코와 존 레논의 전설적인 사진을 오마주해 표지로 실었다. 어쩜 아무런 긴장감이 안 느껴진다. 정지 화면의 캡처 같다. 이건 칭찬이다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/EaOtL2-700x484.jpeg" width="700px" height="484px" />
<figcaption>▲ &lt;A NICE MAGAZINE&gt; 1호. 나오자마자 &lt;i-D&gt;, &lt;DAZED &amp; CONFUSED&gt; 같은 패션 매거진에서 바쁘게 다뤘다<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/lzyecv-640x879.jpeg" width="640px" height="879px" />
<figcaption>▲ 한나 문의 친구 조이스. 패션과 라이프 스타일, 포토그라피가 한 데 섞인, &lt;A NICE MAGAZINE&gt;의 슬로건과 들어맞는 사진 한 장<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/oF6w7f-700x314.jpeg" width="700px" height="314px" />
<figcaption>▲ 그 밖의 사진들<br /><br /></figcaption>
</figure>
<p>&nbsp;</p>
<h2>샌디 킴(Sandy Kim)</h2>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/MGx09L-700x462.jpeg" width="700px" height="462px" />
<figcaption>▲ 샌디 킴의 사진은 일상과 가깝기 때문에 블로그에 그날의 기록처럼 올라오곤 한다. 요 몇 년간은 업데이트가 되지 않았다. 블로그에서 가져온 셀프 사진</figcaption>
</figure>
<p>여성을 모델로 찍는 남자 사진가는 많다. 사진 속에서 은근히 성적인 뉘앙스를 풍기기도 한다. 잠자리를 하고 난 직후를 떠올리게 하거나 셔터를 몇 번 누르고 나면 그런 일이 벌어질 것이라는 암시를 주는 사진들 말이다. 그런 부분에서 여성 사진가들은 억울하다. 사진가 샌디 킴은 10년 전부터 앞뒤 안 가리고 사진을 찍었다. 남자친구의 벗은 몸과 자신의 벗은 몸을 아무렇지 않게. 사랑을 나누고 나서의 결과물까지도 여과 없이 사진으로 담았다. 예쁘게 보이려 결점을 가리고, 심의에 걸릴 만한 것을 거르는 일 따위는 없다. 샌디 킴은 막 20대에 들어섰을 때 사진기를 들고 주변에 일어나는 모든 일을 찍어댔다. 약과 음악, 섹스와 흥분, 쾌락. 빠르게 지나 사라지는 것들을 기억하기 위해 사진기를 들었다고 한다. 샌디 킴이 담은 홀딱 벗고 마리화나를 태우는 친구의 모습, 술을 마시고 난 후의 잔재, 금괴처럼 쌓아 올린 빈 담뱃갑은 외면하고 싶은 장면임과 동시에 너무 순진할 정도로 사실적이다. 이렇게 솔직한 사진을 찍는 사진가는 아직까지 샌디 킴 이외에 보지 못했다.&nbsp;</p>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/iyWnei-700x236.jpeg" width="700px" height="236px" />
<figcaption>▲ 홈페이지에는 성적이고 노골적인 사진만 모은 트리플 엑스 섹션이 있다. 그런데 뭐가 이상한가. 보다 보면 다 너무 당연한 사진들이다. 연인이 샌디 킴에게 보낸 사랑의 문자<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/wsfg23-700x947.jpeg" width="700px" height="947px" />
<figcaption>▲ 고단한 뉴욕의 삶<br /><br /></figcaption>
</figure>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/2BAXCX-700x236.jpeg" width="700px" height="236px" />
<figcaption>▲ 밴드 걸스의 사진을 꾸준히 찍었다. 샌디 킴 사진의 반은 너무나 아름다운 빛을 담고 있다</figcaption>
</figure>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/uchcNJ-700x1405.jpeg" width="700px" height="1405px" />
<figcaption>▲ 그 밖의 사진들</figcaption>
</figure>
<p>&nbsp;</p>
<figure class="image"><img src="https://cdn.indiepost.co.kr/uploads/images/2017/02/itWQWa-539x376.jpeg" width="539px" height="376px" /></figure>
        """.trim().trimIndent()
        assertThat(DomUtil.findAndRemoveWriterInformationFromContent(content)).isEqualTo(expected)
    }
}
