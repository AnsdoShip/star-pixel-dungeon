/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SwordThrow extends MissileWeapon {

	{
		image = ItemSpriteSheet.SWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		bones = false;

		tier = 3;
		baseUses = 1000;
		sticky = false;
	}

	@Override
	public int min(int lvl) {
		return  Math.round(2f * tier) +   //6 base, down from 8
				2 * lvl;                    //scaling unchanged
	}

	@Override
	public int max(int lvl) {
		return  Math.round(4f * tier) +  //15 base, down from 20
				(tier)*lvl;                 //scaling unchanged
	}

	@Override
	public int STRReq(int lvl) { return STRReq(tier+2, lvl);

	}

}
