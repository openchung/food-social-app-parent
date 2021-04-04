/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : db_imooc

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/11/2020 19:06:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `display_order` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 415 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dictionary
-- ----------------------------
INSERT INTO `t_dictionary` VALUES (1, 'TableType           ', 'Regular|大廳', 0);
INSERT INTO `t_dictionary` VALUES (2, 'TableType           ', 'Bar|吧台', 0);
INSERT INTO `t_dictionary` VALUES (3, 'TableType           ', 'Window|靠窗', 0);
INSERT INTO `t_dictionary` VALUES (4, 'TableType           ', 'Outdoor|戶外', 0);
INSERT INTO `t_dictionary` VALUES (5, 'TableType           ', 'Private|包間', 0);
INSERT INTO `t_dictionary` VALUES (7, 'RestaurantTag       ', '24|hours|24小時營業', 0);
INSERT INTO `t_dictionary` VALUES (8, 'RestaurantTag       ', 'Afternoon|tea|下午茶', 0);
INSERT INTO `t_dictionary` VALUES (9, 'RestaurantTag       ', 'All|you|can|eat|自助餐', 0);
INSERT INTO `t_dictionary` VALUES (10, 'RestaurantTag       ', 'Bistros|酒館', 0);
INSERT INTO `t_dictionary` VALUES (11, 'RestaurantTag       ', 'Breakfast|早餐', 0);
INSERT INTO `t_dictionary` VALUES (12, 'RestaurantTag       ', 'Bund|view|外灘風景', 0);
INSERT INTO `t_dictionary` VALUES (13, 'RestaurantTag       ', 'Classic|Shanghai|老上海', 0);
INSERT INTO `t_dictionary` VALUES (14, 'RestaurantTag       ', 'Cocktails|雞尾酒', 0);
INSERT INTO `t_dictionary` VALUES (15, 'RestaurantTag       ', 'Credit|cards|accepted|可刷卡', 0);
INSERT INTO `t_dictionary` VALUES (16, 'RestaurantTag       ', 'Delivery|可送外賣', 0);
INSERT INTO `t_dictionary` VALUES (17, 'RestaurantTag       ', 'Pet|friendly|寵物友好', 0);
INSERT INTO `t_dictionary` VALUES (18, 'RestaurantTag       ', 'Kids|friendly||適合小孩', 0);
INSERT INTO `t_dictionary` VALUES (19, 'RestaurantTag       ', 'Fine|dining|頂級餐廳', 0);
INSERT INTO `t_dictionary` VALUES (20, 'RestaurantTag       ', 'Free|parking|免費停車', 0);
INSERT INTO `t_dictionary` VALUES (21, 'RestaurantTag       ', 'Lounge|酒廊', 0);
INSERT INTO `t_dictionary` VALUES (22, 'RestaurantTag       ', 'Lunch|set|午市套餐', 0);
INSERT INTO `t_dictionary` VALUES (23, 'RestaurantTag       ', 'Group|dining|團體', 0);
INSERT INTO `t_dictionary` VALUES (24, 'RestaurantTag       ', 'Healthy|健康', 0);
INSERT INTO `t_dictionary` VALUES (25, 'RestaurantTag       ', 'Historic|building|歷史建築', 0);
INSERT INTO `t_dictionary` VALUES (26, 'RestaurantTag       ', 'Hotel|restaurant||酒店餐廳', 0);
INSERT INTO `t_dictionary` VALUES (27, 'RestaurantTag       ', 'Ice|cream|冰激淩', 0);
INSERT INTO `t_dictionary` VALUES (28, 'RestaurantTag       ', 'Late|night|dining|夜宵', 0);
INSERT INTO `t_dictionary` VALUES (29, 'RestaurantTag       ', 'Non-smoking|有無煙區', 0);
INSERT INTO `t_dictionary` VALUES (30, 'RestaurantTag       ', 'Notable|wine|list|葡萄酒', 0);
INSERT INTO `t_dictionary` VALUES (32, 'RestaurantTag       ', 'Outdoor|seating|戶外餐桌', 0);
INSERT INTO `t_dictionary` VALUES (33, 'RestaurantTag       ', 'Performance|現場表演', 0);
INSERT INTO `t_dictionary` VALUES (34, 'RestaurantTag       ', 'Romantic||浪漫', 0);
INSERT INTO `t_dictionary` VALUES (35, 'RestaurantTag       ', 'Ramen|日式拉面', 0);
INSERT INTO `t_dictionary` VALUES (36, 'RestaurantTag       ', 'Salads|沙拉', 0);
INSERT INTO `t_dictionary` VALUES (37, 'RestaurantTag       ', 'Sandwiches & Delis|三明治&熟食', 0);
INSERT INTO `t_dictionary` VALUES (38, 'RestaurantTag       ', 'Smoothies|冰沙', 0);
INSERT INTO `t_dictionary` VALUES (39, 'RestaurantTag       ', 'Tapas|西班牙小吃', 0);
INSERT INTO `t_dictionary` VALUES (40, 'RestaurantTag       ', 'Themed|restaurant|主題餐廳', 0);
INSERT INTO `t_dictionary` VALUES (41, 'RestaurantTag       ', 'Villa|別墅', 0);
INSERT INTO `t_dictionary` VALUES (43, 'Cuisine             ', 'American|北美菜', 0);
INSERT INTO `t_dictionary` VALUES (45, 'Cuisine             ', 'Australian|澳洲菜', 0);
INSERT INTO `t_dictionary` VALUES (48, 'Cuisine             ', 'Barbecue|燒烤', 0);
INSERT INTO `t_dictionary` VALUES (50, 'Cuisine             ', 'Beijing|京菜', 0);
INSERT INTO `t_dictionary` VALUES (63, 'Cuisine             ', 'Dongbei|東北菜', 0);
INSERT INTO `t_dictionary` VALUES (65, 'Cuisine             ', 'Hunan|湘菜', 0);
INSERT INTO `t_dictionary` VALUES (68, 'Cuisine             ', 'French|法國菜', 0);
INSERT INTO `t_dictionary` VALUES (70, 'Cuisine             ', 'Fusion|創意菜', 0);
INSERT INTO `t_dictionary` VALUES (71, 'Cuisine             ', 'German|德國菜', 0);
INSERT INTO `t_dictionary` VALUES (72, 'Cuisine             ', 'Grocery|雜貨', 0);
INSERT INTO `t_dictionary` VALUES (73, 'Cuisine             ', 'Halal|清真', 0);
INSERT INTO `t_dictionary` VALUES (76, 'Cuisine             ', 'Hot Pot|火鍋', 0);
INSERT INTO `t_dictionary` VALUES (79, 'Cuisine             ', 'Indian|印度菜', 0);
INSERT INTO `t_dictionary` VALUES (80, 'Cuisine             ', 'Indonesian|印尼菜', 0);
INSERT INTO `t_dictionary` VALUES (81, 'Cuisine             ', 'Italian|意大利菜', 0);
INSERT INTO `t_dictionary` VALUES (82, 'Cuisine             ', 'Japanese|日本料理', 0);
INSERT INTO `t_dictionary` VALUES (84, 'Cuisine             ', 'Jiangxi|贛菜', 0);
INSERT INTO `t_dictionary` VALUES (88, 'Cuisine             ', 'Malaysian|馬來西亞菜', 0);
INSERT INTO `t_dictionary` VALUES (91, 'Cuisine             ', 'Mediterranean|地中海菜', 0);
INSERT INTO `t_dictionary` VALUES (92, 'Cuisine             ', 'Mexican / Tex-Mex|墨西哥菜', 0);
INSERT INTO `t_dictionary` VALUES (94, 'Cuisine             ', 'Other|其他', 0);
INSERT INTO `t_dictionary` VALUES (102, 'Cuisine             ', 'Portuguese|葡國菜', 0);
INSERT INTO `t_dictionary` VALUES (103, 'Cuisine             ', 'Russian|俄國菜', 0);
INSERT INTO `t_dictionary` VALUES (104, 'Cuisine             ', 'Sandwiches & Delis|三明治&簡食', 0);
INSERT INTO `t_dictionary` VALUES (107, 'Cuisine             ', 'Shaoxing|紹興菜', 0);
INSERT INTO `t_dictionary` VALUES (111, 'Cuisine             ', 'Shanghainese|上海菜', 0);
INSERT INTO `t_dictionary` VALUES (112, 'Cuisine             ', 'Singaporean|新加坡菜', 0);
INSERT INTO `t_dictionary` VALUES (113, 'Cuisine             ', 'South American|南美菜', 0);
INSERT INTO `t_dictionary` VALUES (114, 'Cuisine             ', 'Spanish|西班牙菜', 0);
INSERT INTO `t_dictionary` VALUES (115, 'Cuisine             ', 'Steakhouse|牛排店', 0);
INSERT INTO `t_dictionary` VALUES (117, 'Cuisine             ', 'Taiwanese|台灣菜', 0);
INSERT INTO `t_dictionary` VALUES (118, 'Cuisine             ', 'Thai|泰國菜', 0);
INSERT INTO `t_dictionary` VALUES (121, 'Cuisine             ', 'Turkish|土耳其菜', 0);
INSERT INTO `t_dictionary` VALUES (122, 'Cuisine             ', 'Vegetarian|素食', 0);
INSERT INTO `t_dictionary` VALUES (123, 'Cuisine             ', 'Vietnamese|越南菜', 0);
INSERT INTO `t_dictionary` VALUES (124, 'Cuisine             ', 'Wine Bar|紅酒吧', 0);
INSERT INTO `t_dictionary` VALUES (126, 'Cuisine             ', 'Yunnan|雲南菜', 0);
INSERT INTO `t_dictionary` VALUES (129, 'Cuisine             ', 'Zhejiang|浙菜', 0);
INSERT INTO `t_dictionary` VALUES (130, 'nations             ', 'Afghanistan | 阿富汗', 0);
INSERT INTO `t_dictionary` VALUES (131, 'nations             ', 'Albania | 阿爾巴尼亞', 0);
INSERT INTO `t_dictionary` VALUES (132, 'nations             ', 'Algeria | 阿爾及利亞', 0);
INSERT INTO `t_dictionary` VALUES (133, 'nations             ', 'Andorra | 安道爾', 0);
INSERT INTO `t_dictionary` VALUES (134, 'nations             ', 'Angola | 安哥拉', 0);
INSERT INTO `t_dictionary` VALUES (135, 'nations             ', 'Argentina | 阿根廷', 0);
INSERT INTO `t_dictionary` VALUES (136, 'nations             ', 'Armenia | 亞美尼亞', 0);
INSERT INTO `t_dictionary` VALUES (137, 'nations             ', 'Australia | 澳大利亞', 0);
INSERT INTO `t_dictionary` VALUES (138, 'nations             ', 'Austria | 奧地利', 0);
INSERT INTO `t_dictionary` VALUES (139, 'nations             ', 'Azerbaijan | 阿塞拜疆', 0);
INSERT INTO `t_dictionary` VALUES (140, 'nations             ', 'Bahamas | 巴哈馬', 0);
INSERT INTO `t_dictionary` VALUES (141, 'nations             ', 'Bahrain | 巴林', 0);
INSERT INTO `t_dictionary` VALUES (142, 'nations             ', 'Bangladesh | 孟加拉國', 0);
INSERT INTO `t_dictionary` VALUES (143, 'nations             ', 'Barbados | 巴巴多斯', 0);
INSERT INTO `t_dictionary` VALUES (144, 'nations             ', 'Belarus | 白俄羅斯', 0);
INSERT INTO `t_dictionary` VALUES (145, 'nations             ', 'Belgium | 比利時', 0);
INSERT INTO `t_dictionary` VALUES (146, 'nations             ', 'Belize | 伯利茲', 0);
INSERT INTO `t_dictionary` VALUES (147, 'nations             ', 'Benin | 柏林', 0);
INSERT INTO `t_dictionary` VALUES (148, 'nations             ', 'Bhutan | 不丹', 0);
INSERT INTO `t_dictionary` VALUES (149, 'nations             ', 'Bolivia | 玻利維亞', 0);
INSERT INTO `t_dictionary` VALUES (150, 'nations             ', 'Bosnia-Herzegovina | 波斯尼亞和黑塞哥維那', 0);
INSERT INTO `t_dictionary` VALUES (151, 'nations             ', 'Botswana | 博茨瓦納', 0);
INSERT INTO `t_dictionary` VALUES (152, 'nations             ', 'Brazil | 巴西', 0);
INSERT INTO `t_dictionary` VALUES (154, 'nations             ', 'Brunei | 文萊', 0);
INSERT INTO `t_dictionary` VALUES (155, 'nations             ', 'Bulgaria | 保加利亞', 0);
INSERT INTO `t_dictionary` VALUES (156, 'nations             ', 'Burkina | 布基納法索', 0);
INSERT INTO `t_dictionary` VALUES (157, 'nations             ', 'Burma (Myanmar) | 緬甸', 0);
INSERT INTO `t_dictionary` VALUES (158, 'nations             ', 'Burundi | 布隆迪', 0);
INSERT INTO `t_dictionary` VALUES (159, 'nations             ', 'Cambodia | 柬埔寨', 0);
INSERT INTO `t_dictionary` VALUES (160, 'nations             ', 'Cameroon | 喀麥隆', 0);
INSERT INTO `t_dictionary` VALUES (161, 'nations             ', 'Canada | 加拿大', 0);
INSERT INTO `t_dictionary` VALUES (162, 'nations             ', 'Cape Verde Islands | 佛得角群島', 0);
INSERT INTO `t_dictionary` VALUES (163, 'nations             ', 'Chad | 乍得', 0);
INSERT INTO `t_dictionary` VALUES (164, 'nations             ', 'Chile | 智利', 0);
INSERT INTO `t_dictionary` VALUES (166, 'nations             ', 'Colombia | 哥倫比亞', 0);
INSERT INTO `t_dictionary` VALUES (167, 'nations             ', 'Congo | 剛果', 0);
INSERT INTO `t_dictionary` VALUES (168, 'nations             ', 'Costa Rica | 哥斯達黎加', 0);
INSERT INTO `t_dictionary` VALUES (169, 'nations             ', 'Croatia | 克羅地亞', 0);
INSERT INTO `t_dictionary` VALUES (170, 'nations             ', 'Cuba | 古巴', 0);
INSERT INTO `t_dictionary` VALUES (171, 'nations             ', 'Cyprus | 塞浦路斯', 0);
INSERT INTO `t_dictionary` VALUES (172, 'nations             ', 'Czech Republic | 捷克共和國', 0);
INSERT INTO `t_dictionary` VALUES (173, 'nations             ', 'Denmark | 丹麥', 0);
INSERT INTO `t_dictionary` VALUES (174, 'nations             ', 'Djibouti | 吉布提', 0);
INSERT INTO `t_dictionary` VALUES (175, 'nations             ', 'Dominica | 多米尼加', 0);
INSERT INTO `t_dictionary` VALUES (176, 'nations             ', 'Dominican Republic | 多米尼加國共和國', 0);
INSERT INTO `t_dictionary` VALUES (177, 'nations             ', 'Ecuador | 厄瓜多爾', 0);
INSERT INTO `t_dictionary` VALUES (178, 'nations             ', 'Egypt | 埃及', 0);
INSERT INTO `t_dictionary` VALUES (179, 'nations             ', 'El Salvador | 薩爾瓦多', 0);
INSERT INTO `t_dictionary` VALUES (180, 'nations             ', 'England | 英格蘭', 0);
INSERT INTO `t_dictionary` VALUES (181, 'nations             ', 'Eritrea | 厄立特里亞', 0);
INSERT INTO `t_dictionary` VALUES (182, 'nations             ', 'Estonia | 愛沙尼亞', 0);
INSERT INTO `t_dictionary` VALUES (183, 'nations             ', 'Ethiopia | 埃塞俄比亞', 0);
INSERT INTO `t_dictionary` VALUES (184, 'nations             ', 'Fiji | 斐濟', 0);
INSERT INTO `t_dictionary` VALUES (185, 'nations             ', 'Finland | 芬蘭', 0);
INSERT INTO `t_dictionary` VALUES (186, 'nations             ', 'France | 法國', 0);
INSERT INTO `t_dictionary` VALUES (187, 'nations             ', 'Gabon | 加蓬', 0);
INSERT INTO `t_dictionary` VALUES (188, 'nations             ', 'Gambia | 岡比亞', 0);
INSERT INTO `t_dictionary` VALUES (189, 'nations             ', 'Georgia | 格魯吉亞', 0);
INSERT INTO `t_dictionary` VALUES (190, 'nations             ', 'Germany | 德國', 0);
INSERT INTO `t_dictionary` VALUES (191, 'nations             ', 'Ghana | 加納', 0);
INSERT INTO `t_dictionary` VALUES (192, 'nations             ', 'Greece | 希臘', 0);
INSERT INTO `t_dictionary` VALUES (193, 'nations             ', 'Grenada | 格林納達', 0);
INSERT INTO `t_dictionary` VALUES (194, 'nations             ', 'Guatemala | 危地馬拉', 0);
INSERT INTO `t_dictionary` VALUES (195, 'nations             ', 'Guinea | 幾內亞', 0);
INSERT INTO `t_dictionary` VALUES (196, 'nations             ', 'Guyana | 圭亞那', 0);
INSERT INTO `t_dictionary` VALUES (197, 'nations             ', 'Haiti | 海地', 0);
INSERT INTO `t_dictionary` VALUES (198, 'nations             ', 'Netherlands | 荷蘭', 0);
INSERT INTO `t_dictionary` VALUES (199, 'nations             ', 'Honduras | 洪都拉斯', 0);
INSERT INTO `t_dictionary` VALUES (200, 'nations             ', 'Hungary | 匈牙利', 0);
INSERT INTO `t_dictionary` VALUES (201, 'nations             ', 'Iceland | 冰島', 0);
INSERT INTO `t_dictionary` VALUES (202, 'nations             ', 'India | 印度', 0);
INSERT INTO `t_dictionary` VALUES (203, 'nations             ', 'Indonesia | 印度尼西亞', 0);
INSERT INTO `t_dictionary` VALUES (204, 'nations             ', 'Iran | 伊朗', 0);
INSERT INTO `t_dictionary` VALUES (205, 'nations             ', 'Iraq | 伊拉克', 0);
INSERT INTO `t_dictionary` VALUES (206, 'nations             ', 'Ireland | 愛爾蘭', 0);
INSERT INTO `t_dictionary` VALUES (207, 'nations             ', 'Italy | 意大利', 0);
INSERT INTO `t_dictionary` VALUES (208, 'nations             ', 'Jamaica | 牙買加', 0);
INSERT INTO `t_dictionary` VALUES (209, 'nations             ', 'Japan | 日本', 0);
INSERT INTO `t_dictionary` VALUES (210, 'nations             ', 'Jordan | 約旦', 0);
INSERT INTO `t_dictionary` VALUES (211, 'nations             ', 'Kazakhstan | 哈薩克斯坦', 0);
INSERT INTO `t_dictionary` VALUES (212, 'nations             ', 'Kenya | 肯尼亞', 0);
INSERT INTO `t_dictionary` VALUES (213, 'nations             ', 'Kuwait | 科威特', 0);
INSERT INTO `t_dictionary` VALUES (214, 'nations             ', 'Laos | 老撾', 0);
INSERT INTO `t_dictionary` VALUES (215, 'nations             ', 'Latvia | 拉脫維亞', 0);
INSERT INTO `t_dictionary` VALUES (216, 'nations             ', 'Lebanon | 黎巴嫩', 0);
INSERT INTO `t_dictionary` VALUES (217, 'nations             ', 'Liberia | 利比里亞', 0);
INSERT INTO `t_dictionary` VALUES (218, 'nations             ', 'Libya | 利比亞', 0);
INSERT INTO `t_dictionary` VALUES (219, 'nations             ', 'Liechtenstein | 列支敦士登', 0);
INSERT INTO `t_dictionary` VALUES (220, 'nations             ', 'Lithuania | 立陶宛', 0);
INSERT INTO `t_dictionary` VALUES (221, 'nations             ', 'Luxembourg | 盧森堡', 0);
INSERT INTO `t_dictionary` VALUES (222, 'nations             ', 'Macedonia馬其頓', 0);
INSERT INTO `t_dictionary` VALUES (223, 'nations             ', 'Madagascar | 馬達加斯加', 0);
INSERT INTO `t_dictionary` VALUES (224, 'nations             ', 'Malawi | 馬拉維', 0);
INSERT INTO `t_dictionary` VALUES (225, 'nations             ', 'Malaysia | 馬來西亞', 0);
INSERT INTO `t_dictionary` VALUES (226, 'nations             ', 'Maldives | 馬爾代夫', 0);
INSERT INTO `t_dictionary` VALUES (227, 'nations             ', 'Mali | 馬里', 0);
INSERT INTO `t_dictionary` VALUES (228, 'nations             ', 'Malta | 馬耳他', 0);
INSERT INTO `t_dictionary` VALUES (229, 'nations             ', 'Mauritania | 毛里塔尼亞', 0);
INSERT INTO `t_dictionary` VALUES (230, 'nations             ', 'Mauritius | 毛里求斯', 0);
INSERT INTO `t_dictionary` VALUES (231, 'nations             ', 'Mexico | 墨西哥', 0);
INSERT INTO `t_dictionary` VALUES (232, 'nations             ', 'Moldova | 摩爾多瓦', 0);
INSERT INTO `t_dictionary` VALUES (233, 'nations             ', 'Monaco | 摩納哥', 0);
INSERT INTO `t_dictionary` VALUES (234, 'nations             ', 'Mongolia | 蒙古', 0);
INSERT INTO `t_dictionary` VALUES (235, 'nations             ', 'Montenegro | 黑山', 0);
INSERT INTO `t_dictionary` VALUES (236, 'nations             ', 'Morocco | 摩洛哥', 0);
INSERT INTO `t_dictionary` VALUES (237, 'nations             ', 'Mozambique | 莫桑比克', 0);
INSERT INTO `t_dictionary` VALUES (239, 'nations             ', 'Namibia | 納米比亞', 0);
INSERT INTO `t_dictionary` VALUES (240, 'nations             ', 'Nepal | 尼泊爾', 0);
INSERT INTO `t_dictionary` VALUES (242, 'nations             ', 'New Zealand | 新西蘭', 0);
INSERT INTO `t_dictionary` VALUES (243, 'nations             ', 'Nicaragua | 尼加拉瓜', 0);
INSERT INTO `t_dictionary` VALUES (244, 'nations             ', 'Niger | 尼日爾', 0);
INSERT INTO `t_dictionary` VALUES (245, 'nations             ', 'Nigeria | 尼日利亞', 0);
INSERT INTO `t_dictionary` VALUES (246, 'nations             ', 'North Korea | 朝鮮', 0);
INSERT INTO `t_dictionary` VALUES (247, 'nations             ', 'Norway | 挪威', 0);
INSERT INTO `t_dictionary` VALUES (248, 'nations             ', 'Oman | 阿曼', 0);
INSERT INTO `t_dictionary` VALUES (249, 'nations             ', 'Pakistan | 巴基斯坦', 0);
INSERT INTO `t_dictionary` VALUES (250, 'nations             ', 'Panama | 巴拿馬', 0);
INSERT INTO `t_dictionary` VALUES (251, 'nations             ', 'Papua New Guinea | 巴布亞新幾內亞', 0);
INSERT INTO `t_dictionary` VALUES (252, 'nations             ', 'Paraguay | 巴拉圭', 0);
INSERT INTO `t_dictionary` VALUES (253, 'nations             ', 'Peru | 秘魯', 0);
INSERT INTO `t_dictionary` VALUES (254, 'nations             ', 'Philippines | 菲律賓', 0);
INSERT INTO `t_dictionary` VALUES (255, 'nations             ', 'Poland | 波蘭', 0);
INSERT INTO `t_dictionary` VALUES (256, 'nations             ', 'Portugal | 葡萄牙', 0);
INSERT INTO `t_dictionary` VALUES (257, 'nations             ', 'Qatar | 卡塔爾', 0);
INSERT INTO `t_dictionary` VALUES (258, 'nations             ', 'Romania | 羅馬尼亞', 0);
INSERT INTO `t_dictionary` VALUES (259, 'nations             ', 'Russia | 俄羅斯', 0);
INSERT INTO `t_dictionary` VALUES (260, 'nations             ', 'Rwanda | 盧旺達', 0);
INSERT INTO `t_dictionary` VALUES (261, 'nations             ', 'Saudi Arabia | 沙特阿拉伯', 0);
INSERT INTO `t_dictionary` VALUES (262, 'nations             ', 'Scotland | 蘇格蘭', 0);
INSERT INTO `t_dictionary` VALUES (263, 'nations             ', 'Senegal | 塞內加爾', 0);
INSERT INTO `t_dictionary` VALUES (264, 'nations             ', 'Serbia | 塞爾維亞', 0);
INSERT INTO `t_dictionary` VALUES (265, 'nations             ', 'Seychelles | 塞舌爾', 0);
INSERT INTO `t_dictionary` VALUES (266, 'nations             ', 'Sierra Leone | 塞拉里昂', 0);
INSERT INTO `t_dictionary` VALUES (267, 'nations             ', 'Singapore | 新加坡', 0);
INSERT INTO `t_dictionary` VALUES (268, 'nations             ', 'Slovakia | 斯洛伐克', 0);
INSERT INTO `t_dictionary` VALUES (269, 'nations             ', 'Slovenia | 斯洛伐尼亞', 0);
INSERT INTO `t_dictionary` VALUES (270, 'nations             ', 'Solomon Islands | 所羅門群島', 0);
INSERT INTO `t_dictionary` VALUES (271, 'nations             ', 'Somalia | 索馬里', 0);
INSERT INTO `t_dictionary` VALUES (272, 'nations             ', 'South Africa | 南非', 0);
INSERT INTO `t_dictionary` VALUES (273, 'nations             ', 'South Korea | 韓國', 0);
INSERT INTO `t_dictionary` VALUES (274, 'nations             ', 'Spain | 西班牙', 0);
INSERT INTO `t_dictionary` VALUES (275, 'nations             ', 'Sri Lanka | 斯里蘭卡', 0);
INSERT INTO `t_dictionary` VALUES (276, 'nations             ', 'Sudan | 蘇丹', 0);
INSERT INTO `t_dictionary` VALUES (277, 'nations             ', 'Suriname | 蘇里南', 0);
INSERT INTO `t_dictionary` VALUES (278, 'nations             ', 'Swaziland | 斯維士蘭', 0);
INSERT INTO `t_dictionary` VALUES (279, 'nations             ', 'Sweden | 瑞典', 0);
INSERT INTO `t_dictionary` VALUES (280, 'nations             ', 'Switzerland | 瑞士', 0);
INSERT INTO `t_dictionary` VALUES (281, 'nations             ', 'Syria | 敘利亞', 0);
INSERT INTO `t_dictionary` VALUES (282, 'nations             ', 'Taiwan | 台灣', 0);
INSERT INTO `t_dictionary` VALUES (283, 'nations             ', 'Tajikistan | 塔吉克斯坦', 0);
INSERT INTO `t_dictionary` VALUES (284, 'nations             ', 'Tanzania | 坦桑尼亞', 0);
INSERT INTO `t_dictionary` VALUES (285, 'nations             ', 'Thailand | 泰國', 0);
INSERT INTO `t_dictionary` VALUES (286, 'nations             ', 'Togo | 多哥', 0);
INSERT INTO `t_dictionary` VALUES (287, 'nations             ', 'Trinidad and Tobago | 特里尼達和多巴哥', 0);
INSERT INTO `t_dictionary` VALUES (289, 'nations             ', 'Tunisia | 突尼斯', 0);
INSERT INTO `t_dictionary` VALUES (290, 'nations             ', 'Turkey | 土耳其', 0);
INSERT INTO `t_dictionary` VALUES (291, 'nations             ', 'Turkmenistan | 土庫曼斯坦', 0);
INSERT INTO `t_dictionary` VALUES (292, 'nations             ', 'Tuvalu | 圖瓦盧', 0);
INSERT INTO `t_dictionary` VALUES (293, 'nations             ', 'Uganda | 烏幹達', 0);
INSERT INTO `t_dictionary` VALUES (294, 'nations             ', 'Ukraine | 烏克蘭', 0);
INSERT INTO `t_dictionary` VALUES (295, 'nations             ', 'United Arab Emirates | 阿拉伯聯合大公國', 0);
INSERT INTO `t_dictionary` VALUES (296, 'nations             ', 'United Kingdom  | 聯合王國', 0);
INSERT INTO `t_dictionary` VALUES (297, 'nations             ', 'U.S.A. | 美國', 0);
INSERT INTO `t_dictionary` VALUES (298, 'nations             ', 'Uruguay | 烏拉圭', 0);
INSERT INTO `t_dictionary` VALUES (299, 'nations             ', 'Uzbekistan | 烏茲別克斯坦', 0);
INSERT INTO `t_dictionary` VALUES (300, 'nations             ', 'Vanuatu | 瓦努阿圖', 0);
INSERT INTO `t_dictionary` VALUES (301, 'nations             ', 'Vatican City | 梵蒂岡', 0);
INSERT INTO `t_dictionary` VALUES (302, 'nations             ', 'Venezuela | 委內瑞拉', 0);
INSERT INTO `t_dictionary` VALUES (303, 'nations             ', 'Vietnam | 越南', 0);
INSERT INTO `t_dictionary` VALUES (304, 'nations             ', 'Wales | 威爾士', 0);
INSERT INTO `t_dictionary` VALUES (305, 'nations             ', 'Western Samoa | 西薩摩亞', 0);
INSERT INTO `t_dictionary` VALUES (306, 'nations             ', 'Yemen | 也門', 0);
INSERT INTO `t_dictionary` VALUES (307, 'nations             ', 'Yugoslavia | 南斯拉夫', 0);
INSERT INTO `t_dictionary` VALUES (308, 'nations             ', 'Zaire | 紮伊爾', 0);
INSERT INTO `t_dictionary` VALUES (309, 'nations             ', 'Zambia | 讚比亞', 0);
INSERT INTO `t_dictionary` VALUES (310, 'nations             ', 'Zimbabwe | 津巴布韋', 0);
INSERT INTO `t_dictionary` VALUES (311, 'nations             ', 'Anhui | 安徽', 0);
INSERT INTO `t_dictionary` VALUES (312, 'nations             ', 'Fujian | 福建', 0);
INSERT INTO `t_dictionary` VALUES (313, 'nations             ', 'Gansu | 甘肅', 0);
INSERT INTO `t_dictionary` VALUES (314, 'nations             ', 'Guangdong | 廣東', 0);
INSERT INTO `t_dictionary` VALUES (315, 'nations             ', 'Guizhou | 貴州', 0);
INSERT INTO `t_dictionary` VALUES (316, 'nations             ', 'Hainan | 海南', 0);
INSERT INTO `t_dictionary` VALUES (317, 'nations             ', 'Hebei | 河北', 0);
INSERT INTO `t_dictionary` VALUES (318, 'nations             ', 'Heilongjiang | 黑龍江', 0);
INSERT INTO `t_dictionary` VALUES (319, 'nations             ', 'Henan | 河南', 0);
INSERT INTO `t_dictionary` VALUES (320, 'nations             ', 'Hubei | 湖北', 0);
INSERT INTO `t_dictionary` VALUES (321, 'nations             ', 'Hunan | 湖南', 0);
INSERT INTO `t_dictionary` VALUES (322, 'nations             ', 'Jiangsu | 江蘇', 0);
INSERT INTO `t_dictionary` VALUES (323, 'nations             ', 'Jiangxi | 江西', 0);
INSERT INTO `t_dictionary` VALUES (324, 'nations             ', 'Jilin | 吉林', 0);
INSERT INTO `t_dictionary` VALUES (325, 'nations             ', 'Liaoning | 遼寧', 0);
INSERT INTO `t_dictionary` VALUES (326, 'nations             ', 'Qinghai | 青海', 0);
INSERT INTO `t_dictionary` VALUES (327, 'nations             ', 'Shaanxi | 陜西', 0);
INSERT INTO `t_dictionary` VALUES (328, 'nations             ', 'Shandong | 山東', 0);
INSERT INTO `t_dictionary` VALUES (329, 'nations             ', 'Shanxi | 山西', 0);
INSERT INTO `t_dictionary` VALUES (330, 'nations             ', 'Sichuan | 四川', 0);
INSERT INTO `t_dictionary` VALUES (331, 'nations             ', 'Yunnan | 雲南', 0);
INSERT INTO `t_dictionary` VALUES (332, 'nations             ', 'Zhejiang | 浙江', 0);
INSERT INTO `t_dictionary` VALUES (333, 'nations             ', 'Guangxi | 廣西', 0);
INSERT INTO `t_dictionary` VALUES (334, 'nations             ', 'Inner Mongolia | 內蒙古', 0);
INSERT INTO `t_dictionary` VALUES (335, 'nations             ', 'Ningxia | 寧夏', 0);
INSERT INTO `t_dictionary` VALUES (336, 'nations             ', 'Xinjiang | 新疆', 0);
INSERT INTO `t_dictionary` VALUES (337, 'nations             ', 'Tibet | 西藏', 0);
INSERT INTO `t_dictionary` VALUES (338, 'nations             ', 'Beijing | 北京', 0);
INSERT INTO `t_dictionary` VALUES (339, 'nations             ', 'Chongqing | 重慶', 0);
INSERT INTO `t_dictionary` VALUES (340, 'nations             ', 'Shanghai | 上海', 0);
INSERT INTO `t_dictionary` VALUES (341, 'nations             ', 'Tianjin | 天津', 0);
INSERT INTO `t_dictionary` VALUES (342, 'nations             ', 'Hong Kong | 香港', 0);
INSERT INTO `t_dictionary` VALUES (343, 'nations             ', 'Macau | 澳門', 0);
INSERT INTO `t_dictionary` VALUES (346, 'RestaurantTag       ', 'Wifi|無線上網', 0);
INSERT INTO `t_dictionary` VALUES (349, 'RestaurantTag       ', 'Good|View|有景觀位', 0);
INSERT INTO `t_dictionary` VALUES (351, 'RestaurantTag       ', 'Big|Party|大型宴會', 0);
INSERT INTO `t_dictionary` VALUES (352, 'RestaurantTag       ', 'Birthday|Party|生日宴會', 0);
INSERT INTO `t_dictionary` VALUES (353, 'RestaurantTag       ', 'BYOB|自帶酒水', 0);
INSERT INTO `t_dictionary` VALUES (354, 'RestaurantStyle     ', '朋友聚餐', 0);
INSERT INTO `t_dictionary` VALUES (355, 'RestaurantStyle     ', '家庭聚會', 0);
INSERT INTO `t_dictionary` VALUES (356, 'RestaurantStyle     ', '隨便吃吃', 0);
INSERT INTO `t_dictionary` VALUES (357, 'RestaurantStyle     ', '休閑小憩', 0);
INSERT INTO `t_dictionary` VALUES (358, 'RestaurantStyle     ', '情侶約會', 0);
INSERT INTO `t_dictionary` VALUES (359, 'RestaurantStyle     ', '商務宴請', 0);
INSERT INTO `t_dictionary` VALUES (360, 'InviteStatus        ', '即將接洽', 0);
INSERT INTO `t_dictionary` VALUES (361, 'InviteStatus        ', '等待老板決定', 0);
INSERT INTO `t_dictionary` VALUES (362, 'InviteStatus        ', '合同簽署中', 0);
INSERT INTO `t_dictionary` VALUES (363, 'InviteStatus        ', '合同已經簽訂', 0);
INSERT INTO `t_dictionary` VALUES (370, 'Cuisine', 'All-You-Can-Eat|自助餐', 0);
INSERT INTO `t_dictionary` VALUES (371, 'Cuisine', 'Bar|酒吧', 0);
INSERT INTO `t_dictionary` VALUES (372, 'Cuisine', 'Cafe|咖啡廳', 0);
INSERT INTO `t_dictionary` VALUES (373, 'Cuisine', 'Cantonese|粵菜', 0);
INSERT INTO `t_dictionary` VALUES (374, 'Cuisine', 'Dessert|甜品', 0);
INSERT INTO `t_dictionary` VALUES (375, 'Cuisine', 'Global Cuisine|環球美食', 0);
INSERT INTO `t_dictionary` VALUES (376, 'Cuisine', 'Middle Eastern|中東菜', 0);
INSERT INTO `t_dictionary` VALUES (377, 'Cuisine', 'Southeast Asian|東南亞菜', 0);
INSERT INTO `t_dictionary` VALUES (378, 'Cuisine', 'Zhejiang|浙江菜', 0);
INSERT INTO `t_dictionary` VALUES (379, 'Cuisine', 'Fast Casual|小吃快餐', 0);
INSERT INTO `t_dictionary` VALUES (380, 'nations', 'Israel', 0);
INSERT INTO `t_dictionary` VALUES (381, 'nations', 'East Timor', 0);
INSERT INTO `t_dictionary` VALUES (382, 'nations', 'Central African Republic', 0);
INSERT INTO `t_dictionary` VALUES (383, 'nations', 'S?o Tomé and Principe', 0);
INSERT INTO `t_dictionary` VALUES (384, 'nations', 'Ivory Coast', 0);
INSERT INTO `t_dictionary` VALUES (385, 'nations', 'Lesotho', 0);
INSERT INTO `t_dictionary` VALUES (386, 'nations', 'Equatorial Guinea', 0);
INSERT INTO `t_dictionary` VALUES (387, 'nations', 'Guinea Bissau', 0);
INSERT INTO `t_dictionary` VALUES (400, 'Cuisine', 'Sushi|壽司', 0);
INSERT INTO `t_dictionary` VALUES (401, 'Cuisine', 'British|英國菜', 0);
INSERT INTO `t_dictionary` VALUES (402, 'Cuisine', 'Dim Sum|早茶點心', 0);
INSERT INTO `t_dictionary` VALUES (403, 'Cuisine', 'Xibei / Xinjiang|西北菜/新疆菜', 0);
INSERT INTO `t_dictionary` VALUES (405, 'Cuisine', 'Guizhou|黔菜', 0);
INSERT INTO `t_dictionary` VALUES (406, 'Cuisine', 'Pizza|披薩', 0);
INSERT INTO `t_dictionary` VALUES (408, 'Cuisine', 'Seafood|海鮮', 0);
INSERT INTO `t_dictionary` VALUES (409, 'Cuisine', 'Anhui|徽菜', 0);
INSERT INTO `t_dictionary` VALUES (411, 'Cuisine', 'Sichuan|川菜', 0);
INSERT INTO `t_dictionary` VALUES (412, 'Cuisine', 'Korean|韓國料理', 0);
INSERT INTO `t_dictionary` VALUES (413, 'Cuisine', 'Juice & Beverages|果汁飲料', 0);
INSERT INTO `t_dictionary` VALUES (414, 'Cuisine', 'Bakery & Pastries|面包烘焙', 0);

-- ----------------------------
-- Table structure for t_diners
-- ----------------------------
DROP TABLE IF EXISTS `t_diners`;
CREATE TABLE `t_diners`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵稱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '頭像',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '角色',
  `is_valid` tinyint(1) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_diners
-- ----------------------------
INSERT INTO `t_diners` VALUES (1, 'abc', '昵稱st', '13888888888', 'abc@imooc.com', 'e10adc3949ba59abbe56e057f20f883e', '/abc', 'ROLE_USER', 1, '2020-11-06 16:17:52', '2020-11-06 16:17:55');
INSERT INTO `t_diners` VALUES (2, 'test', 'test', '13666666666', NULL, 'e10adc3949ba59abbe56e057f20f883e', '/test', 'ROLE_USER', 1, '2020-11-12 12:01:13', '2020-11-12 12:01:13');
INSERT INTO `t_diners` VALUES (3, 'test2', 'test2', '13666666667', NULL, 'e10adc3949ba59abbe56e057f20f883e', '/test2', 'ROLE_USER', 1, '2020-11-12 17:47:12', '2020-11-12 17:47:12');
INSERT INTO `t_diners` VALUES (5, 'aaa', 'aaa', '12311112222', NULL, 'e10adc3949ba59abbe56e057f20f883e', '/aaa', 'ROLE_USER', 1, '2020-11-13 12:29:49', '2020-11-13 12:29:49');

-- ----------------------------
-- Table structure for t_feed
-- ----------------------------
DROP TABLE IF EXISTS `t_feed`;
CREATE TABLE `t_feed`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '內容',
  `fk_diner_id` int(11) NULL DEFAULT NULL,
  `praise_amount` int(11) NULL DEFAULT NULL COMMENT '點讚數量',
  `comment_amount` int(11) NULL DEFAULT NULL COMMENT '評論數量',
  `fk_restaurant_id` int(11) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `is_valid` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_feed
-- ----------------------------

-- ----------------------------
-- Table structure for t_follow
-- ----------------------------
DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `diner_id` int(11) NULL DEFAULT NULL,
  `follow_diner_id` int(11) NULL DEFAULT NULL,
  `is_valid` tinyint(1) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_followeddiner_valid`(`follow_diner_id`, `is_valid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_follow
-- ----------------------------

-- ----------------------------
-- Table structure for t_restaurant
-- ----------------------------
DROP TABLE IF EXISTS `t_restaurant`;
CREATE TABLE `t_restaurant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'the En Name of the restaurant',
  `CnName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `X` double NULL DEFAULT NULL,
  `Y` double NULL DEFAULT NULL,
  `Location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'En location of the restaurant',
  `CnLocation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Area` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'city.district.neighbourhood\r\nExample: Shanghai.Xuhui.Xujiahui',
  `CnArea` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Traffic` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'the information/descripton of the restaurant',
  `Telephone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Phone of the restaurant',
  `Email` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Website` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Cuisine` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AveragePrice` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AvgLunchPrice` decimal(19, 0) NULL DEFAULT NULL COMMENT 'Average price of lunch',
  `Introduction` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Indtroduction of the restaurant',
  `Status` int(11) NULL DEFAULT 0 COMMENT '1=Opened 0=Closed',
  `CreateDT` datetime(0) NULL DEFAULT NULL,
  `IsValid` smallint(1) NULL DEFAULT 1 COMMENT '1=Valid 0=Invalid',
  `Thumbnail` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'pics at the list, value would be:\r\nbasepath/original/picname',
  `OpenHours` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LikeVotes` int(10) NULL DEFAULT 0 COMMENT 'the percentage of people like it',
  `DislikeVotes` int(10) NULL DEFAULT 0 COMMENT 'How many people votes',
  `Amenities` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '設備',
  `Tags` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'tags of the restaurant',
  `OpenDate` datetime(0) NULL DEFAULT NULL,
  `closeDate` datetime(0) NULL DEFAULT NULL,
  `CityId` int(11) NULL DEFAULT 21 COMMENT '城市id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_isvalid`(`IsValid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_restaurant
-- ----------------------------
INSERT INTO `t_restaurant` VALUES (14, '1931 Pub', '名古', 31.2158508275268, 121.461839852847, '112 Maoming Nan Lu, near Nanchang Lu', '茂名南路112號, 近南昌路', 'Xuhui.Fmr French Concession', '', '', '021 6472 5264', '', '', 'Shanghainese, Chinese', '¥¥', 0, '', NULL, '2014-05-04 19:26:28', 1, '', '', 1, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (15, '2001 Hong Kong Teahouse', '2001港式茶餐', 31.21385, 121.46051, '55 Shaanxi Nan Lu, near Changle Lu', '陜西南路55號, 近長樂路', 'Xuhui.Fmr French Concession', '', '', '021 5467 0205', '', '', 'Dim Sum, Chinese', '¥', 0, '', NULL, '2014-05-04 19:26:28', 4, '', '', 0, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (16, '2nd floor', '2nd floor', 31.2162, 121.447998, '2/F, 810 Changle Lu,near Changshu Lu', '長樂路810號2樓, 近常熟路', 'Xuhui.Fmr French Concession', '', '', '13761133471', '', 'http://www.2ndfloor.asia', 'Cafe', '¥', 0, '', NULL, '2014-05-04 19:26:28', 3, '', '', 1, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (17, '400 Celsius', '400 Celsius', 31.19436, 121.43797, '1 Hongqiao Lu, 1/F, Grand Gateway, near Caoxi Bei Lu, Metro Line 1 Xujiahui Station', '虹橋路1號港匯廣場1樓, 近漕溪北路, 地鐵1號線徐家匯站', 'Xuhui.Xujiahui', '', '', '021 6447 0770', '', '', 'Steakhouse', '¥¥¥¥', 0, '', NULL, '2014-05-04 19:26:28', 3, '', '', 0, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (18, '5 on the Bund', '5 on the Bund', 31.234482, 121.490753, 'Five on the Bund,20 Guangdong Lu, near Zhongshan Dong Yi Lu', '廣東路20號, 近中山東一路', 'Huangpu.The Bund', '', '', '', '', '', 'Global Cuisine', '¥¥¥¥', 0, '', NULL, '2014-05-04 19:26:28', 3, '', '', 0, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (19, '5 Tables Bistro', '5桌餐廳', 31.2174481541175, 121.47318647082, '210 Danshui Lu, near Zizhong Lu', '淡水路210號, 近自忠路', 'Luwan.Xintiandi', '', '', '021 3304 1205', '', 'www.weibo.com/5tables', 'European', '¥¥¥¥', 0, '', NULL, '2014-05-04 19:26:28', 4, '', '', 0, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (20, '57 Du Xiang', '57度湘', 31.2250117063411, 121.47824432639, '138 Huaihai Zhong Lu, Infinity Plaza, 4/F, Room 401, near Longmen Lu', '淮海路138號無限度廣場4樓401室, 近龍門路', 'Xuhui.Huaihai Zhong Lu', '', '', '021 3315 0057', '', '', 'Hunan, Chinese', '¥', 0, '', NULL, '2014-05-04 19:26:28', 1, 'restaurant/20/restaurant/T/160_160/1399622680327.JPG', 'Daily 11am-9pm', 17, 5, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (21, '609 Pho', '609 Pho', 31.237629, 121.438797, '609 Anyuan Lu, near Jiaozhou Lu', '安源路609號, 近膠州路', 'Jing\'an', '', '', '18201753996', '', '', 'Vietnamese', '¥', 0, '', NULL, '2014-05-04 19:26:28', 4, '', '', 0, 0, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);
INSERT INTO `t_restaurant` VALUES (22, '70s Restaurant', '70後飯吧', 31.2398228737211, 121.438096413353, '1217 Changde Lu, near Changshou Lu', '常德路1217號, 近長壽路', 'Putuo', '', '', '021 6040 2808', '', '', 'Chinese', '¥¥', 0, '', NULL, '2014-05-04 19:26:28', 1, 'restaurant/22/restaurant/160_160/14075670693130533.JPG', '', 7, 2, '', '', '2016-01-04 11:22:23', '2016-01-04 11:22:23', 21);

-- ----------------------------
-- Table structure for t_seckill_vouchers
-- ----------------------------
DROP TABLE IF EXISTS `t_seckill_vouchers`;
CREATE TABLE `t_seckill_vouchers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_voucher_id` int(11) NULL DEFAULT NULL,
  `amount` int(11) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `is_valid` int(11) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_seckill_vouchers
-- ----------------------------

-- ----------------------------
-- Table structure for t_voucher
-- ----------------------------
DROP TABLE IF EXISTS `t_voucher`;
CREATE TABLE `t_voucher`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代金券標題',
  `thumbnail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '縮略圖',
  `amount` int(11) NULL DEFAULT NULL COMMENT '抵扣金額',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售價',
  `status` int(10) NULL DEFAULT NULL COMMENT '-1=過期 0=下架 1=上架',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '過期時間',
  `redeem_restaurant_id` int(10) NULL DEFAULT NULL COMMENT '驗證餐廳',
  `stock` int(11) NULL DEFAULT 0 COMMENT '庫存',
  `stock_left` int(11) NULL DEFAULT 0 COMMENT '剩余數量',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `clause` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用條款',
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `is_valid` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_voucher
-- ----------------------------

-- ----------------------------
-- Table structure for t_voucher_order
-- ----------------------------
DROP TABLE IF EXISTS `t_voucher_order`;
CREATE TABLE `t_voucher_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` int(11) NULL DEFAULT NULL,
  `fk_voucher_id` int(11) NULL DEFAULT NULL,
  `fk_diner_id` int(11) NULL DEFAULT NULL,
  `qrcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '圖片地址',
  `payment` tinyint(4) NULL DEFAULT NULL COMMENT '0=微信支付 1=支付寶支付',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '訂單狀態：-1=已取消 0=未支付 1=已支付 2=已消費 3=已過期',
  `fk_seckill_id` int(11) NULL DEFAULT NULL COMMENT '如果是搶購訂單時，搶購訂單的id',
  `order_type` int(11) NULL DEFAULT NULL COMMENT '訂單類型：0=正常訂單 1=搶購訂單',
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `is_valid` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_voucher_order
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
