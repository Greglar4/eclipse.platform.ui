/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.widgets.CTabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Shows the View Menu
 * <p>
 * Replacement for: ShowViewMenuAction
 * </p>
 * 
 * @since 3.3
 * 
 */
public class ShowViewMenuHandler extends AbstractEvaluationHandler {

	private Expression enabledWhen;

	public ShowViewMenuHandler() {
		registerEnablement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			if (site instanceof PartSite) {
				final MPart model = ((PartSite) site).getModel();
				Composite partContainer = (Composite) model.getWidget();
				if (partContainer != null) {
					Composite parent = partContainer.getParent();
					while (parent != null && parent instanceof Composite) {
						if (parent instanceof CTabFolder) {
							CTabFolder ctf = (CTabFolder) parent;
							final Control topRight = ctf.getTopRight();
							if (topRight instanceof Composite) {
								for (Control child : ((Composite) topRight).getChildren()) {
									if (child instanceof ToolBar
											&& "ViewMenu".equals(child.getData())) { //$NON-NLS-1$
										ToolBar tb = (ToolBar) child;
										ToolItem ti = tb.getItem(0);
										Event sevent = new Event();
										sevent.type = SWT.Selection;
										sevent.widget = ti;
										ti.notifyListeners(SWT.Selection, sevent);
									}
								}
							}
							return null;
						}
						parent = parent.getParent();
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.internal.AbstractEvaluationHandler#getEnabledWhenExpression
	 * ()
	 */
	protected Expression getEnabledWhenExpression() {
		// TODO Auto-generated method stub
		if (enabledWhen == null) {
			enabledWhen = new Expression() {
				public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
					// IWorkbenchPart part = InternalHandlerUtil
					// .getActivePart(context);
					// if (part != null) {
					// PartPane pane = ((PartSite) part.getSite()).getPane();
					// if ((pane instanceof ViewPane)
					// && ((ViewPane) pane).hasViewMenu()) {
					return EvaluationResult.TRUE;
					// }
					// }
					// return EvaluationResult.FALSE;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.core.expressions.Expression#collectExpressionInfo
				 * (org.eclipse.core.expressions.ExpressionInfo)
				 */
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_PART_NAME);
				}
			};
		}
		return enabledWhen;
	}

}
