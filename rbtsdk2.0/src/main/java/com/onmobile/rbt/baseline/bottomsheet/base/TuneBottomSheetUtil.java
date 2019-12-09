package com.onmobile.rbt.baseline.bottomsheet.base;

import android.text.TextUtils;

import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.rbt.baseline.model.ContactModelDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TuneBottomSheetUtil {
    public static boolean isSetForSpecialCallers(List<PlayRuleDTO> playRuleDTOList) {
        /*if (mPlayRuleDTOList == null)
            return false;
        for (PlayRuleDTO playRuleDTO : mPlayRuleDTOList)
            if (playRuleDTO != null && playRuleDTO.getCallingparty() != null
                    && playRuleDTO.getCallingparty().getId() != null
                    && !playRuleDTO.getCallingparty().getId().equals("0")) {
                return true;
            }
        return false;*/

        if (playRuleDTOList != null) {
            for (PlayRuleDTO playRuleDTO : playRuleDTOList) {
                if (playRuleDTO == null)
                    continue;
                final CallingParty callingParty = playRuleDTO.getCallingparty();
                if (callingParty == null)
                    continue;
                final String party = callingParty.getId();
                if (!TextUtils.isEmpty(party) && !party.equals("0"))
                    return true;
            }
        }
        return false;
    }

    public static boolean isSpecialCallerExists(List<PlayRuleDTO> playRuleDTOList, String mobileNumber) {
        if (playRuleDTOList != null && !TextUtils.isEmpty(mobileNumber)) {
            for (PlayRuleDTO playRuleDTO : playRuleDTOList) {
                if (playRuleDTO != null && playRuleDTO.getCallingparty() != null
                        && !TextUtils.isEmpty(playRuleDTO.getCallingparty().getId())
                        && playRuleDTO.getCallingparty().getId().contains(mobileNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return count of already set and currently selected contacts
     *
     * @return count
     */
    public static int getTotalCount(List<PlayRuleDTO> playRuleDTOList, Map<String, ContactModelDTO> contactModel, Map<String, Boolean> alreadySetContacts) {
        int count;
        count = getSpecialCallerCount(playRuleDTOList) - getUncheckedSpecialCaller(alreadySetContacts).size();
        count += contactModel != null ? contactModel.size() : 0;
        return count;
    }

    public static List<String> getCallingParties(List<PlayRuleDTO> playRuleDTOList) {
        /*if (mPlayRuleDTOList != null && mPlayRuleDTOList.size() > 0 && isSetForSpecialCallers(mPlayRuleDTOList)) {
            List<String> callingParty = new ArrayList<>();
            for (PlayRuleDTO playRuleDTO : mPlayRuleDTOList) {
                if (playRuleDTO == null)
                    callingParty.add();
            }
        }*/

        if (playRuleDTOList != null) {
            List<String> callingParty = null;
            for (PlayRuleDTO playRuleDTO : playRuleDTOList) {
                if (playRuleDTO == null)
                    continue;
                final CallingParty callingPartyObj = playRuleDTO.getCallingparty();
                if (callingPartyObj == null)
                    continue;
                if (callingParty == null)
                    callingParty = new ArrayList<>();
                final String party = callingPartyObj.getId();
                if (!TextUtils.isEmpty(party) && !party.equals("0"))
                    callingParty.add(party);
            }
            return callingParty;
        }
        return null;
    }

    public static int getSpecialCallerCount(List<PlayRuleDTO> playRuleDTOList) {
        int count = 0;
        if (isSetForSpecialCallers(playRuleDTOList)) {
            for (PlayRuleDTO playRuleDTO : playRuleDTOList) {
                if (playRuleDTO == null)
                    continue;
                final CallingParty callingPartyObj = playRuleDTO.getCallingparty();
                if (callingPartyObj == null)
                    continue;
                final String party = callingPartyObj.getId();
                if (!TextUtils.isEmpty(party) && !party.equals("0"))
                    count++;
            }
        }
        return count;
    }

    public static boolean anySpecialCallerUnchecked(Map<String, Boolean> alreadySetContacts) {
        if (alreadySetContacts != null) {
            for (boolean unchecked :
                    alreadySetContacts.values()) {
                if (!unchecked)
                    return true;
            }
        }
        return false;
    }

    public static Map<String, Boolean> getUncheckedSpecialCaller(Map<String, Boolean> alreadySetContacts) {
        Map<String, Boolean> uncheckedList = new HashMap<>();
        if (alreadySetContacts != null) {
            for (Map.Entry<String, Boolean> entry :
                    alreadySetContacts.entrySet()) {
                if (!entry.getValue())
                    uncheckedList.put(entry.getKey(), entry.getValue());
            }
        }
        return uncheckedList;
    }
}
