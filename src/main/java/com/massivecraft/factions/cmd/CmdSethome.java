package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSethome extends FCommand {

    public CmdSethome() {
        this.aliases.add("sethome");

        //this.requiredArgs.add("");
        this.optionalArgs.put("faction tag", "mine");

        this.permission = Permission.SETHOME.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeColeader = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        if (!Conf.homesEnabled) {
            fme.msg(TL.COMMAND_SETHOME_DISABLED);
            return;
        }

        Faction faction = this.argAsFaction(0, myFaction);
        if (faction == null) {
            return;
        }

        if (!fme.isAdminBypassing()) {
            Access access = myFaction.getAccess(fme, PermissableAction.SETHOME);
            if (access != Access.ALLOW && (fme.getRole() != Role.ADMIN && fme.getRole() != Role.COLEADER) && !Permission.SETHOME_ANY.has(sender, true)) {
                fme.msg(TL.GENERIC_FPERM_NOPERMISSION, "set home");
                return;
            }
        }

        // Can the player set the faction home HERE?
        if (!Permission.BYPASS.has(me) &&
                Conf.homesMustBeInClaimedTerritory &&
                Board.getInstance().getFactionAt(new FLocation(me)) != faction) {
            fme.msg(TL.COMMAND_SETHOME_NOTCLAIMED);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!payForCommand(Conf.econCostSethome, TL.COMMAND_SETHOME_TOSET, TL.COMMAND_SETHOME_FORSET)) {
            return;
        }

        faction.setHome(me.getLocation());

        faction.msg(TL.COMMAND_SETHOME_SET, fme.describeTo(myFaction, true));
        faction.sendMessage(p.cmdBase.cmdHome.getUseageTemplate());
        if (faction != myFaction) {
            fme.msg(TL.COMMAND_SETHOME_SETOTHER, faction.getTag(fme));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETHOME_DESCRIPTION;
    }

}