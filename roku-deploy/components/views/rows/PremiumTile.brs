sub init()
    m.scaledElements = m.top.findNode("scaledElements")
    m.molthlyPlan = m.top.findNode("molthlyPlan")
    m.price = m.top.findNode("price")


    m.chooseMonthlyBtn = m.top.findNode("chooseMonthlyBtn")    
    m.chooseMonthlyBtn.observeField("buttonSelected", "chooseMonthlyBtnSelected")
end sub

sub onFocusChanged()
    if m.top.focusPercent >= 0.5 and m.top.rowListHasFocus = true
       m.chooseMonthlyBtn.setFocus(true)
    else
       m.chooseMonthlyBtn.setFocus(false)
    end if

end sub


function updateLayout(event as object)
    if (m.top.width > 0 and m.top.height > 0) then
        if m.originalWidth = invalid or m.originalWidth = 0 then
            m.originalWidth = m.top.width
            m.originalHeight = m.top.height
        end if

        updateLayoutWithDimensions(m.originalWidth, m.originalHeight)
    end if
end function

function updateLayoutWithDimensions(width as float, height as float)
    ' m.background.width = width
    ' m.background.height = height
end function

function onContentChanged(nodeEvent as object)

    content = nodeEvent.getData()
    m.molthlyPlan.text = content.title
    m.price.text = content.price
   
    m.chooseMonthlyBtn.imgSrc = {
        id: "chooseMonthly",
        label: "Purchase Now" '+ content.title,
        unFocusImg: "pkg://images/fill_r8.9.png",
        focusImg: "pkg://images/fill_r8.9.png",
        width: 350,
        height: 60
    }
    m.chooseMonthlyBtn.translation = [(400 - m.chooseMonthlyBtn.imgSrc.width) / 2, 400]
end function
