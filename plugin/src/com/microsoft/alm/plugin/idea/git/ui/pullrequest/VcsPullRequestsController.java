// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.idea.git.ui.pullrequest;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.microsoft.alm.plugin.events.ServerEvent;
import com.microsoft.alm.plugin.idea.common.ui.common.tabs.TabControllerImpl;
import com.microsoft.alm.plugin.idea.common.ui.common.tabs.TabImpl;
import com.microsoft.alm.plugin.idea.git.actions.ComparePullRequestAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * Controller for the Version Control Pull Requests Tab
 */
public class VcsPullRequestsController extends TabControllerImpl<VcsPullRequestsModel> {
    private static final String EVENT_NAME = "Pull Requests";

    public VcsPullRequestsController(final @NotNull Project project) {
        super(new TabImpl(new VcsPullRequestsForm(), EVENT_NAME), new VcsPullRequestsModel(project),
                new ServerEvent[]{ServerEvent.PULL_REQUESTS_CHANGED});
    }

    @Override
    protected void performAction(final ActionEvent e) {
        if (VcsPullRequestsForm.CMD_COMPARE_PULL_REQUEST.equals(e.getActionCommand())) {
            AnAction action = ActionManager.getInstance().getAction("ComparePullRequestAction");

            if (action != null) {
                DataContext baseContext = DataManager.getInstance().getDataContext();
                DataContext customContext = SimpleDataContext.getSimpleContext(ComparePullRequestAction.PULL_REQUEST_ID_KEY, model.getSelectedPullRequestId(), baseContext);
                AnActionEvent event = AnActionEvent.createFromAnAction(
                        action,
                        null,
                        ActionPlaces.UNKNOWN,
                        customContext
                );
                action.actionPerformed(event);
            }
        } else if (VcsPullRequestsForm.CMD_OPEN_SELECTED_ITEM_IN_BROWSER.equals(e.getActionCommand())) {
            //pop up menu - open PR link in web
            model.openSelectedItemsLink();
        } else if (VcsPullRequestsForm.CMD_ABANDON_SELECTED_PR.equals(e.getActionCommand())) {
            //pop up menu - abandon PR
            model.abandonSelectedPullRequest();
        } else {
            super.performAction(e);
        }
    }
}
