/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/no-static-element-interactions */

import Profile from "components/common/Profile"
import StateBadge from "components/common/StateBadge"

const ArticleItem = ({data, onSelectArticle}) => {
  return (
    <div
      className="relative mx-auto my-4 h-[425px] w-72 overflow-hidden rounded-xl border border-stroke "
      onClick={() => {
        onSelectArticle(data)
      }}
    >
      <StateBadge state={data.boardType} />

      <img
        src={data.thumbnail}
        alt="imag"
        className="size-[300px] object-contain"
      />
      <div className="p-4">
        <div className=" mb-4 flex h-8 items-center justify-between">
          <Profile nickname={data.userNickname} image={data.userImage} />
          <div className="flex flex-row space-x-2">
            <img
              src="/icons/icon-comment.svg"
              alt="Comment Icon"
              className="w-5"
            />
            <div>{data.commentCount}</div>
          </div>
        </div>
        <div className=" text-overflow hover:text-clip">{data.title}</div>
      </div>
    </div>
  )
}
export default ArticleItem
