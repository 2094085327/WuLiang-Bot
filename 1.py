import difflib
import sys

from typing import Union

import json
from pathlib import Path
import re
import datetime

import requests


def save_json(data: dict, path: Union[Path, str] = None, encoding: str = 'utf-8'):
    """
    保存json文件
    :param data: json数据
    :param path: 保存路径
    :param encoding: 编码
    """
    if isinstance(path, str):
        path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    json.dump(data, path.open('w', encoding=encoding), ensure_ascii=False, indent=4)


def load_json(path: Union[Path, str], encoding: str = 'utf-8') -> dict:
    """
    说明：
        读取本地json文件，返回json字典。
    参数：
        :param path: 文件路径
        :param encoding: 编码，默认为utf-8
        :return: json字典
    """
    if isinstance(path, str):
        path = Path(path)
    if not path.exists():
        save_json({}, path, encoding)
    return json.load(path.open('r', encoding=encoding))


def get_short_name(name: str):
    """
        获取角色或武器的短名（2个字）
        :param name: 角色或武器名
        :return: 短名字符串
    """
    short_name = load_json(path=Path(__file__).parent //'resources'/'GenShinInfo'/ 'json_data' / 'short_name.json')
    return name if name not in short_name.keys() else short_name[name]


def get_id_by_name(name: str):
    """
        根据角色名字获取角色的id
        :param name: 角色名
        :return: id字符串
    """
    alias_file = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'alias.json')
    name_list = alias_file['roles']
    for role_id, alias in name_list.items():
        if name in alias:
            return role_id


def get_name_by_id(role_id: str):
    """
        根据角色id获取角色名
        :param role_id: 角色id
        :return: 角色名字符串
    """
    alias_file = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'alias.json')
    name_list = alias_file['roles']
    if role_id in name_list:
        return name_list[role_id][0]
    else:
        return None


def get_alias_by_name(name: str):
    """
        根据角色名字获取角色的别名
        :param name: 角色名
        :return: 别名列表
    """
    alias_file = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'alias.json')
    name_list = alias_file['roles']
    for r in name_list.values():
        if name in r:
            return r
    return None


def get_match_alias(msg: str, type: str = 'roles', single_to_dict: bool = False) -> Union[str, list, dict]:
    """
        根据字符串消息，获取与之相似或匹配的角色、武器、原魔名
        :param msg: 消息
        :param type: 匹配类型，有roles、weapons、monsters
        :param single_to_dict: 是否将角色单结果也转换成{角色:id}字典
        :return: 匹配的字符串、列表或字典
    """
    alias_file = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'alias.json')
    alias_list = alias_file[type]
    if msg in ['风主', '岩主', '雷主']:
        return msg
    elif type == 'roles':
        possible = {}
        for role_id, alias in alias_list.items():
            match_list = difflib.get_close_matches(msg, alias, cutoff=0.6, n=3)
            if msg in match_list:
                return {alias[0]: role_id} if single_to_dict else alias[0]
            elif match_list:
                possible[alias[0]] = role_id
        if len(possible) == 1:
            return {list(possible.keys())[0]: possible[list(possible.keys())[0]]} if single_to_dict else \
                list(possible.keys())[0]
        return possible
    elif type == 'weapons':
        possible = []
        for name, alias in alias_list.items():
            match_list = difflib.get_close_matches(msg, alias, cutoff=0.4, n=3)
            if msg in match_list:
                return name
            elif match_list:
                possible.append(name)
        return possible
    elif type == 'monsters':
        match_list = difflib.get_close_matches(msg, alias_list, cutoff=0.4, n=5)
        return match_list[0] if len(match_list) == 1 else match_list


role_element = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'role_element.json')
role_skill = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'role_skill.json')
role_talent = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'role_talent.json')
weapon = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'weapon.json')
prop_list = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'prop.json')
artifact_list = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'artifact.json')
ra_score = load_json(path=Path(__file__).parent /'resources'/'GenShinInfo'/ 'json_data' / 'score.json')


class PlayerInfo:
    def __init__(self, uid: str):
# Path()
        self.path = Path(__file__).parent /'resources'/'GenShinInfo'/ 'user'  /f'{uid}.json'
        print(self.path)
        self.data = load_json(path=self.path)
        self.player_info = self.data['玩家信息'] if '玩家信息' in self.data else {}
        self.roles = self.data['角色'] if '角色' in self.data else {}

    def set_player(self, data: dict):
        self.player_info['昵称'] = data.get('nickname', 'unknown')
        self.player_info['等级'] = data.get('level', 'unknown')
        self.player_info['世界等级'] = data.get('worldLevel', 'unknown')
        self.player_info['签名'] = data.get('signature', 'unknown')
        self.player_info['成就'] = data.get('finishAchievementNum', 'unknown')
        self.player_info['角色列表'] = dictList_to_list(data.get('showAvatarInfoList'))
        self.player_info['名片列表'] = data.get('showNameCardIdList', 'unknown')
        self.player_info['头像'] = data['profilePicture']['avatarId']
        self.player_info['更新时间'] = datetime.datetime.strftime(datetime.datetime.now(), '%Y-%m-%d %H:%M:%S')

    def set_role(self, data: dict):
        role_info = {}
        role_name = get_name_by_id(str(data['avatarId']))
        if role_name not in ['unknown', 'None']:
            role_info['名称'] = role_name
            role_info['角色ID'] = data['avatarId']
            role_info['等级'] = int(data['propMap']['4001']['val'])
            role_info['好感度'] = data['fetterInfo']['expLevel']
            if role_name in ['荧', '空']:
                traveler_skill = role_skill['Name'][list(data['skillLevelMap'].keys())[-1]]
                find_element = re.search(r'(风|雷|岩|草|水|火|冰)', traveler_skill).group(1)
                role_info['元素'] = find_element
                role_name = find_element + '主'
            else:
                role_info['元素'] = role_element[role_name]

            if 'talentIdList' in data:
                if len(data['talentIdList']) >= 3:
                    data['skillLevelMap'][list(data['skillLevelMap'].keys())[ra_score['Talent'][role_name][0]]] += 3
                if len(data['talentIdList']) >= 5:
                    data['skillLevelMap'][list(data['skillLevelMap'].keys())[ra_score['Talent'][role_name][1]]] += 3

            role_info['天赋'] = []
            for skill in data['skillLevelMap']:
                skill_detail = {'名称': role_skill['Name'][skill], '等级': data['skillLevelMap'][skill],
                                '图标': role_skill['Icon'][skill]}
                role_info['天赋'].append(skill_detail)
            if role_info['名称'] == '神里绫华':
                role_info['天赋'][0], role_info['天赋'][-1] = role_info['天赋'][-1], role_info['天赋'][0]
                role_info['天赋'][2], role_info['天赋'][-1] = role_info['天赋'][-1], role_info['天赋'][2]
            if role_info['名称'] == '安柏':
                role_info['天赋'][0], role_info['天赋'][-1] = role_info['天赋'][-1], role_info['天赋'][0]
            if role_info['名称'] in ['空', '荧']:
                role_info['天赋'][0], role_info['天赋'][-1] = role_info['天赋'][-1], role_info['天赋'][0]
                role_info['天赋'][1], role_info['天赋'][-1] = role_info['天赋'][-1], role_info['天赋'][1]
            if role_info['名称'] == '达达利亚':
                role_info['天赋'][0]['等级'] += 1

            role_info['命座'] = []
            if 'talentIdList' in data:
                for talent in data['talentIdList']:
                    talent_detail = {'名称': role_talent['Name'][str(talent)],
                                     '图标': role_talent['Icon'][str(talent)]}
                    role_info['命座'].append(talent_detail)

            prop = {}
            prop['基础生命'] = round(data['fightPropMap']['1'])
            prop['额外生命'] = round(data['fightPropMap']['2000'] - prop['基础生命'])
            prop['基础攻击'] = round(data['fightPropMap']['4'])
            prop['额外攻击'] = round(data['fightPropMap']['2001'] - prop['基础攻击'])
            prop['基础防御'] = round(data['fightPropMap']['7'])
            prop['额外防御'] = round(data['fightPropMap']['2002'] - prop['基础防御'])
            prop['暴击率'] = round(data['fightPropMap']['20'], 3)
            prop['暴击伤害'] = round(data['fightPropMap']['22'], 3)
            prop['元素精通'] = round(data['fightPropMap']['28'])
            prop['元素充能效率'] = round(data['fightPropMap']['23'], 3)
            prop['治疗加成'] = round(data['fightPropMap']['26'], 3)
            prop['受治疗加成'] = round(data['fightPropMap']['27'], 3)
            prop['伤害加成'] = [round(data['fightPropMap']['30'], 3)]
            for i in range(40, 47):
                prop['伤害加成'].append(round(data['fightPropMap'][str(i)], 3))
            role_info['属性'] = prop

            weapon_info = {}
            weapon_data = data['equipList'][-1]
            weapon_info['名称'] = weapon['Name'][weapon_data['flat']['nameTextMapHash']]
            weapon_info['图标'] = weapon_data['flat']['icon']
            weapon_info['类型'] = weapon['Type'][weapon_data['flat']['nameTextMapHash']]
            weapon_info['等级'] = weapon_data['weapon']['level']
            weapon_info['星级'] = weapon_data['flat']['rankLevel']
            if 'promoteLevel' in weapon_data['weapon']:
                weapon_info['突破等级'] = weapon_data['weapon']['promoteLevel']
            else:
                weapon_info['突破等级'] = 0
            if 'affixMap' in weapon_data['weapon']:
                weapon_info['精炼等级'] = list(weapon_data['weapon']['affixMap'].values())[0] + 1
            else:
                weapon_info['精炼等级'] = 1
            weapon_info['基础攻击'] = weapon_data['flat']['weaponStats'][0]['statValue']
            try:
                weapon_info['副属性'] = {'属性名': prop_list[weapon_data['flat']['weaponStats'][1]['appendPropId']],
                                         '属性值': weapon_data['flat']['weaponStats'][1]['statValue']}
            except IndexError:
                weapon_info['副属性'] = {'属性名': '无属性', '属性值': 0}
            weapon_info['特效'] = '待补充'
            role_info['武器'] = weapon_info

            artifacts = []
            for artifact in data['equipList'][:-1]:
                artifact_info = {}
                artifact_info['名称'] = artifact_list['Name'][artifact['flat']['icon']]
                artifact_info['图标'] = artifact['flat']['icon']
                artifact_info['部位'] = artifact_list['Piece'][artifact['flat']['icon'].split('_')[-1]][1]
                artifact_info['所属套装'] = artifact_list['Mapping'][artifact_info['名称']]
                artifact_info['等级'] = artifact['reliquary']['level'] - 1
                artifact_info['星级'] = artifact['flat']['rankLevel']
                artifact_info['主属性'] = {'属性名': prop_list[artifact['flat']['reliquaryMainstat']['mainPropId']],
                                           '属性值': artifact['flat']['reliquaryMainstat']['statValue']}
                artifact_info['词条'] = []
                for reliquary in artifact['flat']['reliquarySubstats']:
                    artifact_info['词条'].append({'属性名': prop_list[reliquary['appendPropId']],
                                                  '属性值': reliquary['statValue']})
                artifacts.append(artifact_info)
            role_info['圣遗物'] = artifacts
            role_info['更新时间'] = datetime.datetime.strftime(datetime.datetime.now(), '%Y-%m-%d %H:%M:%S')
            self.roles[role_info['名称']] = role_info

    def get_player_info(self):
        return self.player_info

    def get_update_roles_list(self):
        return self.player_info['角色列表']

    def get_roles_list(self):
        return list(self.roles.keys())

    def get_roles_info(self, role_name):
        if role_name in self.roles:
            return self.roles[role_name]
        else:
            return None

    def save(self):
        self.data['玩家信息'] = self.player_info
        self.data['角色'] = self.roles
        save_json(data=self.data, path=self.path)


def dictList_to_list(data):
    if not isinstance(data, list):
        return 'unknown'
    new_data = {}
    for d in data:
        name = get_name_by_id(str(d['avatarId']))
        new_data[name] = d['avatarId']
    return new_data


def artifact_value(role_prop: dict, prop_name: str, prop_value: float, effective: dict):
    """
    计算圣遗物单词条的有效词条数
    :param role_prop: 角色基础属性
    :param prop_name: 属性名
    :param prop_value: 属性值
    :param effective: 有效词条列表
    :return: 评分
    """
    prop_map = {'攻击力': 4.975, '生命值': 4.975, '防御力': 6.2, '暴击率': 3.3, '暴击伤害': 6.6, '元素精通': 19.75,
                '元素充能效率': 5.5}
    if prop_name in effective.keys() and prop_name in ['攻击力', '生命值', '防御力']:
        return round(prop_value / role_prop[prop_name] * 100 / prop_map[prop_name] * effective[prop_name], 2)
    if prop_name.replace('百分比', '') in effective.keys():
        return round(
            prop_value / prop_map[prop_name.replace('百分比', '')] * effective[prop_name.replace('百分比', '')], 2)
    return 0


def artifact_total_value(role_prop: dict, artifact: dict, effective: dict):
    """
    计算圣遗物总有效词条数以及评分
    :param role_prop: 角色基础属性
    :param artifact: 圣遗物信息
    :param effective: 有效词条列表
    :return: 总词条数，评分
    """
    new_role_prop = {'攻击力': role_prop['基础攻击'], '生命值': role_prop['基础生命'],
                     '防御力': role_prop['基础防御']}
    value = 0
    for i in artifact['词条']:
        value += artifact_value(new_role_prop, i['属性名'], i['属性值'], effective)
    value = round(value, 2)
    return value, round(value / get_expect_score(effective) * 100, 1)


def get_effective(role_name: str, role_weapon: str, artifacts: list, element: str = '风'):
    """
    根据角色的武器、圣遗物来判断获取该角色有效词条列表
    :param role_name: 角色名
    :param role_weapon: 角色武器
    :param artifacts: 角色圣遗物列表
    :param element: 角色元素，仅需主角传入
    :return: 有效词条列表
    """
    if role_name in ['荧', '空']:
        role_name = str(element) + '主'
    if role_name in ra_score['Role']:
        if len(artifacts) < 5:
            return ra_score['Role'][role_name]['常规']
        if role_name == '钟离':
            if artifacts[-2]['主属性']['属性名'] == '岩元素伤害加成':
                return ra_score['Role'][role_name]['岩伤']
            elif artifacts[-2]['主属性']['属性名'] in ['物理伤害加成', '火元素伤害加成', '冰元素伤害加成']:
                return ra_score['Role'][role_name]['武神']
        if role_name == '班尼特' and artifacts[-2]['主属性']['属性名'] == '火元素伤害加成':
            return ra_score['Role'][role_name]['输出']
        if role_name == '甘雨':
            suit = get_artifact_suit(artifacts)
            if suit and ('乐团' in suit[0][0] or (len(suit) == 2 and '乐团' in suit[1][0])):
                return ra_score['Role'][role_name]['融化']
        if role_name == '申鹤' and artifacts[-2]['主属性']['属性名'] == '冰元素伤害加成':
            return ra_score['Role'][role_name]['输出']
        if role_name == '七七' and artifacts[-2]['主属性']['属性名'] == '物理伤害加成':
            return ra_score['Role'][role_name]['输出']
        if role_name in ['枫原万叶', '温迪', '砂糖'] and artifacts[-2]['主属性']['属性名'] == '风元素伤害加成':
            return ra_score['Role'][role_name]['输出']
        if '西风' in role_weapon and '西风' in ra_score['Role'][role_name]:
            return ra_score['Role'][role_name]['西风']
        return ra_score['Role'][role_name]['常规']
    else:
        return {'攻击力': 1, '暴击率': 1, '暴击伤害': 1}


def get_expect_score(effective: dict):
    """
    计算单个圣遗物小毕业所需的期望词条数
    :param effective: 有效词条列表
    :return: 期望词条数
    """
    total = 0
    if len(effective.keys()) == 2:
        average = 15 / 5
    elif effective.keys() == '西风':
        average = 17 / 5
    elif len(effective.keys()) == 3:
        average = 24 / 5
    elif len(effective.keys()) == 4:
        average = 28 / 5
    else:
        average = 30 / 5
    for name, value in effective.items():
        total += value * average
    return round(total / len(effective.keys()), 2)


def check_effective(prop_name: str, effective: dict):
    """
    检查词条是否有效
    :param prop_name: 词条属性名
    :param effective: 有效词条列表
    :return: 是否有效
    """
    if '攻击力' in effective and '攻击力' in prop_name:
        return True
    if '生命值' in effective and '生命值' in prop_name:
        return True
    if '防御力' in effective and '防御力' in prop_name:
        return True
    return prop_name in effective


def get_artifact_suit(artifacts: list):
    """
    获取圣遗物套装
    :param artifacts: 圣遗物列表
    :return: 套装列表
    """
    suit = []
    suit2 = []
    final_suit = []
    for artifact in artifacts:
        suit.append(artifact['所属套装'])
    for s in suit:
        if s not in suit2 and 1 < suit.count(s) < 4:
            suit2.append(s)
        if suit.count(s) >= 4:
            for r in artifacts:
                if r['所属套装'] == s:
                    return [(s, r['图标']), (s, r['图标'])]
    for r in artifacts:
        if r['所属套装'] in suit2:
            final_suit.append((r['所属套装'], r['图标']))
            suit2.remove(r['所属套装'])
    return final_suit


def get_enka_data(uid):
    url = f'https://enka.network/u/{uid}/__data.json'
    resp = requests.get(url=url, headers={'User-Agent': 'LittlePaimon/2.0'})
    data = resp.json()
    return data


if __name__ == '__main__':
    print("路径：", Path(__file__).parent )
    uid = ""
    for i in sys.argv[1:]:
        uid = i
    enka_data = get_enka_data(uid)
    player_info = PlayerInfo(uid)
    player_info.set_player(enka_data['playerInfo'])
    for role in enka_data['avatarInfoList']:
        player_info.set_role(role)
    player_info.save()
