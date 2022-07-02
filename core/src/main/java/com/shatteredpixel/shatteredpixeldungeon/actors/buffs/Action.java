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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration.REGENERATION_DELAY;
import static com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator.HX2;
import static com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator.HX3;
import static com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator.HX4;
import static com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator.HX5;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;





public class Action extends Buff implements Hero.Doom {

	private static final float STEP = 10f;

	public static final float COLDLEVEL1 = 100f;
	public static final float COLDLEVEL2 = 200f;
	public static final float COLDLEVEL3 = 350f;
	public static final float COLDLEVEL4 = 550f;
	public static final float COLDLEVEL5 = 800f;

	private float level;
	//private float partialDamage;

	private static final String LEVEL = "level";
	//private static final String PARTIALDAMAGE 	= "partialDamage";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
		//bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		level = bundle.getFloat(LEVEL);
		//partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	@Override
	public boolean act() {

		if (Dungeon.level.locked
				|| target.buff(WellFed.class) != null
				|| target.buff(ScrollOfChallenge.ChallengeArena.class) != null) {
			spend(STEP);
			return true;
		}

		if (target.isAlive() && target instanceof Hero) {

			Hero hero = (Hero) target;

			if (!isStarving()) {

				/*partialDamage += STEP * target.HT/1000f;

				//if (partialDamage > 1){
					//target.damage( (int)partialDamage, this);
					partialDamage -= (int)partialDamage;
				}
				
			} else {*/

				float newLevel = level + STEP;
				if (newLevel >= COLDLEVEL1 && level < COLDLEVEL2) {

					GLog.n(Messages.get(this, "level1"));
					//hero.resting = false;
					//hero.damage( 1, this );

					//hero.interrupt();打断动作

				} else if (newLevel >= COLDLEVEL2 && level < COLDLEVEL3) {

					GLog.w(Messages.get(this, "level2"));

					/*if (!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_FOOD)){
						GLog.p(Messages.get(Guidebook.class, "hint"));
						GameScene.flashForDocument(Document.GUIDE_FOOD);
					}*/

				} else if (newLevel >= COLDLEVEL3 && level < COLDLEVEL4) {

					GLog.w(Messages.get(this, "level3"));

				} else if (newLevel >= COLDLEVEL4 && level < COLDLEVEL5) {

					GLog.w(Messages.get(this, "level4"));

				}
				level = newLevel;

			}
			
			spend( target.buff( Shadows.class ) == null ? STEP : STEP * 1.5f );

		} else {

			GLog.w(Messages.get(this, "level5"));
			diactivate();

		}

		return true;
	}

	public void satisfy( float energy ) {

		Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
		if (buff != null && buff.isCursed()){
			energy *= 0.67f;
			GLog.n( Messages.get(this, "cursedhorn") );
		}

		//affectHunger( energy, false );
	}

	//public void affectHunger(float energy ){
		//affectHunger( energy, false );
	//}

	/*public void affectHunger(float energy, boolean overrideLimits ) {

		if (energy < 0 && target.buff(WellFed.class) != null){
			target.buff(WellFed.class).left += energy;
			BuffIndicator.refreshHero();
			return;
		}

		level -= energy;
		if (level < 0 && !overrideLimits) {
			level = 0;
		} else if (level > LEVEL5) {
			float excess = level - LEVEL5;
			level = LEVEL5;
			//partialDamage += excess * (target.HT/1000f);
		}

		BuffIndicator.refreshHero();
	}*/

	public boolean isStarving() {
		return level >= COLDLEVEL5;
	}

	public int hunger() {
		return (int)Math.ceil(level);
	}

	@Override
	public int icon() {
		if (level < COLDLEVEL1) {
			return BuffIndicator.NONE;
		} else if (level < COLDLEVEL2) {
			return BuffIndicator.HX1;
		} else if (level < COLDLEVEL3) {
			return BuffIndicator.HX2;
		} else if (level < COLDLEVEL4) {
			return BuffIndicator.HX3;
		} else if (level < COLDLEVEL5) {
			return BuffIndicator.HX4;
		} else {
			return BuffIndicator.HX5;
		}
	}

	@Override
	public String toString() {
	    if (level < COLDLEVEL1) {
		    REGENERATION_DELAY = 10;
		    return Messages.get(this, "hx2");
	    } else if (level < COLDLEVEL2) {
			REGENERATION_DELAY = 15;
			return Messages.get(this, "hx1");
			//return是返回他所在的最后一行指令，因此下方的指令将不会运行
		} else if (level < COLDLEVEL3) {
			REGENERATION_DELAY = 20;
			return Messages.get(this, "hx2");

		} else if (level < COLDLEVEL4) {
			REGENERATION_DELAY = 30;
			return Messages.get(this, "hx3");

		} else if (level < COLDLEVEL5) {
			REGENERATION_DELAY = 45;
			return Messages.get(this, "hx4");

		} else {
			REGENERATION_DELAY = 60;
			return Messages.get(this, "hx5");
		}
	}

	@Override
	public String desc() {
		String result;
		if (level < HX2) {
			result = Messages.get(this, "desc_intro_hx1");
		} else if (level < HX3) {
			result = Messages.get(this, "desc_intro_hx2");
		} else if (level < HX4) {
			result = Messages.get(this, "desc_intro_hx3");
		} else if (level < HX5) {
			result = Messages.get(this, "desc_intro_hx4");
		} else {
			result = Messages.get(this, "desc_intro_hx5");
		}

		result += Messages.get(this, "desc");

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}


}
